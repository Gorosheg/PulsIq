@file:Suppress("MissingPermission")
@file:OptIn(FlowPreview::class)

package gorosheg.pulsiq.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanResult
import android.bluetooth.le.ScanSettings
import android.content.Context
import android.os.ParcelUuid
import gorosheg.pulsiq.bluetooth.model.BleDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.definition.Callbacks
import java.util.UUID

internal class HeartBeatDataSourceImpl(
    private val context: Context,
    private val scanner: BluetoothLeScanner?,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : HeartBeatDataSource {

    private var gatt: BluetoothGatt? = null
    private var lastDevice: BluetoothDevice? = null
    private var isUserDisconnected = false
    private var reconnectionJob: Job? = null

    private val heartRateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _devices = MutableStateFlow<Map<String, DiscoveredDevice>>(emptyMap())
    val availableDevices: StateFlow<List<BleDevice>> = _devices
        .map { m ->
            m.values
                .sortedByDescending { it.rssi }
                .map { it.toBleDevice() }
        }
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(type: Int, result: ScanResult) {
            val dev = result.device ?: return
            val address = dev.address ?: return
            val connectable = result.isConnectable

            _devices.update { map ->
                map + (address to DiscoveredDevice(
                    device = dev,
                    name = dev.name ?: "Unknown",
                    rssi = result.rssi,
                    isConnectable = connectable
                ))
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            val updates = results.mapNotNull { r ->
                val dev = r.device ?: return@mapNotNull null
                val key = dev.address ?: return@mapNotNull null
                val connectable = r.isConnectable
                key to DiscoveredDevice(
                    device = dev,
                    name = dev.name ?: "Unknown",
                    rssi = r.rssi,
                    isConnectable = connectable
                )
            }.toMap()
            if (updates.isNotEmpty()) _devices.update { it + updates }
        }

        override fun onScanFailed(errorCode: Int) {
            // можно залогировать/пробросить в UI, если потребуется
        }
    }

    override fun subscribeHeartRateFlow(): Flow<Int> {
        return heartRateFlow
            .debounce(1000L)
            .distinctUntilChanged()
    }

    override fun subscribeAvailableDevicesFlow(): Flow<List<BleDevice>> {
        return availableDevices
            .debounce(1000L)
            .distinctUntilChanged()
    }

    override fun startScan() {
        isUserDisconnected = false

        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(heartRateServiceUUID))
            .build()

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        scanner?.startScan(listOf(filter), settings, scanCallback)
    }

    override fun stopScan() {
        scanner?.stopScan(scanCallback)
    }

    override fun connect(address: String, connected: (Boolean) -> Unit) {
        try {
            isUserDisconnected = false
            reconnectionJob?.cancel()
            reconnectionJob = null

            val fromCache = _devices.value[address]?.device

            val device = fromCache ?: run {
                val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val adapter = manager.adapter
                adapter?.getRemoteDevice(address)
            }

            requireNotNull(device) { "Устройство с адресом $address не найдено" }

            lastDevice = device

            scope.launch(Dispatchers.Main.immediate) {
                connectToGatt(connected::invoke::invoke)
            }

        } catch (_: Throwable) {
            connected.invoke(false)
        }
    }

    override fun disconnect() {
        isUserDisconnected = true
        reconnectionJob?.cancel()
        reconnectionJob = null

        stopScan()
        safeCloseGatt()
    }

    private fun connectToGatt(connected: (Boolean) -> Unit) {
        scope.launch(Dispatchers.Main.immediate) {
            safeCloseGatt()
            val device = lastDevice ?: return@launch

            gatt = device.connectGatt(
                context,
                false,
                onPulseChanged(
                    callback = { pulse -> heartRateFlow.value = pulse },
                    onDisconnected = ::startReconnectionAttempts
                )
            )

            connected.invoke(gatt != null)
        }
    }

    private fun startReconnectionAttempts() {
        if (isUserDisconnected) return
        reconnectionJob?.cancel()
        reconnectionJob = scope.launch {
            delay(RECONNECT_DELAY_MS)
            connectToGatt {}
        }
    }

    private fun safeCloseGatt() {
        try {
            gatt?.disconnect()
            gatt?.close()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        gatt = null
    }

    private fun DiscoveredDevice.toBleDevice() = BleDevice(
        name = name,
        address = device.address ?: "00:00:00:00:00:00",
        rssi = rssi,
        isConnectable = isConnectable
    )

    companion object {

        internal val heartRateMeasurementUUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        internal val cccdUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        internal val heartRateServiceUUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")

        private const val RECONNECT_DELAY_MS = 1000L
    }
}

private data class DiscoveredDevice(
    val device: BluetoothDevice,
    val name: String,
    val rssi: Int,
    val isConnectable: Boolean
)