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
    private var connectionJob: Job? = null

    private val heartRateFlow: MutableStateFlow<Int> = MutableStateFlow(0)

    private val _devices = MutableStateFlow<List<BleDevice>>(emptyList())
    val availableDevices: StateFlow<List<BleDevice>> = _devices
        .map { m ->
            m.sortedByDescending { it.rssi }
        }
        .distinctUntilChanged()
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    private val scanCallback = object : ScanCallback() {

        override fun onScanResult(callbackType: Int, result: ScanResult) {
            val device: BluetoothDevice = result.device ?: return

            if (callbackType == ScanSettings.CALLBACK_TYPE_MATCH_LOST) {
                _devices.update { list ->
                    list.filterNot { it.device.address == device.address }
                }
                return
            }

            _devices.update { list ->
                val updated = list.filterNot { it.device.address == device.address }
                updated + device.convertToBleDevice(result)
            }
        }

        override fun onBatchScanResults(results: MutableList<ScanResult>) {
            val updates = results.map { result ->
                val device: BluetoothDevice = result.device ?: return
                device.convertToBleDevice(result)
            }
            if (updates.isNotEmpty()) _devices.update { it + updates }
        }

        override fun onScanFailed(errorCode: Int) {
            // можно пробросить в UI
        }
    }

    private fun BluetoothDevice.convertToBleDevice(
        result: ScanResult
    ): BleDevice {
        return BleDevice(
            device = this,
            name = name ?: "Unknown",
            rssi = result.rssi,
            isConnectable = result.isConnectable
        )
    }

    override fun subscribeHeartRateFlow(): Flow<Int> {
        return heartRateFlow
            .debounce(1000L)
            .distinctUntilChanged()
    }

    override fun subscribeAvailableDevicesFlow(): Flow<List<BleDevice>> {
        return availableDevices
    }

    override fun connect(address: String, connected: (Boolean) -> Unit) {
        try {
            isUserDisconnected = false
            reconnectionJob?.cancel()
            reconnectionJob = null

            val fromCache: BluetoothDevice? = _devices.value.find { it.device.address == address }?.device

            val device = fromCache ?: run {
                val manager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
                val adapter = manager.adapter
                adapter?.getRemoteDevice(address)
            }

            requireNotNull(device) { "Устройство с адресом $address не найдено" }

            lastDevice = device

            connectionJob = scope.launch(Dispatchers.Main.immediate) {
                connectToGatt(connected)
            }

        } catch (_: Throwable) {
            connected.invoke(false)
        }
    }

    override fun disconnect() {
        isUserDisconnected = true
        reconnectionJob?.cancel()
        reconnectionJob = null
        connectionJob?.cancel()
        connectionJob = null
        stopScan()
        safeCloseGatt()
    }

    override fun startScan() {
        isUserDisconnected = false

        val filter = ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(heartRateServiceUUID))
            .build()

        val settings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH or ScanSettings.CALLBACK_TYPE_MATCH_LOST)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setReportDelay(0)
            .build()

        scanner?.startScan(listOf(filter), settings, scanCallback)
    }

    override fun stopScan() {
        scanner?.stopScan(scanCallback)
    }

    private fun connectToGatt(connected: (Boolean) -> Unit) {
        safeCloseGatt()
        val device = lastDevice ?: return

        gatt = device.connectGatt(
            context,
            false,
            onPulseChanged(
                callback = { pulse -> heartRateFlow.value = pulse },
                connected = connected,
                onDisconnected = ::startReconnectionAttempts
            )
        )
    }

    private fun startReconnectionAttempts() {
        if (isUserDisconnected) return
        reconnectionJob?.cancel()
        reconnectionJob = scope.launch(Dispatchers.Main.immediate) {
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

    companion object {
        internal val heartRateMeasurementUUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        internal val cccdUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        internal val heartRateServiceUUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
    }
}