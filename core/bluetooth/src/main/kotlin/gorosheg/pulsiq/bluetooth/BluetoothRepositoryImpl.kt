@file:Suppress("MissingPermission")
@file:OptIn(FlowPreview::class)

package gorosheg.pulsiq.bluetooth

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import gorosheg.pulsiq.bluetooth.model.DomainBluetoothDevice
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

internal class BluetoothRepositoryImpl(
    private val scope: CoroutineScope,
    private val bluetoothConnector: BluetoothConnector,
    private val bluetoothScanner: BluetoothScanner,
    private val filter: ScanFilter,
    private val settings: ScanSettings,
    private val manager: BluetoothManager
) : BluetoothRepository {

    private var lastDevice: BluetoothDevice? = null
    private var isUserConnected = true
    private var reconnectionJob: Job? = null
    private var connectionJob: Job? = null

    private val _heartRateFlow: MutableStateFlow<Int?> = MutableStateFlow(null)
    override val heartRateFlow: Flow<Int> = _heartRateFlow
        .filterNotNull()
        .debounce(1000L)
        .distinctUntilChanged()

    private val _availableDevices = MutableStateFlow<List<DomainBluetoothDevice>>(emptyList())
    override val availableDevices: Flow<List<DomainBluetoothDevice>> = _availableDevices
        .map { devices -> devices.sortedByDescending { it.rssi } }
        .distinctUntilChanged()
        .stateIn(scope, SharingStarted.Eagerly, emptyList())

    override val connectedDevice: MutableStateFlow<String?> = MutableStateFlow("")

    init {
        bluetoothScanner.initScanCallback()
            .onEach { state ->
                when (state) {
                    is BluetoothScannerState.OnCallbackTypeMatchLost -> {
                        _availableDevices.update { list ->
                            list.filterNot { it.device.address == state.device.address }
                        }
                    }

                    is BluetoothScannerState.OnDeviceReceived -> {
                        _availableDevices.update { list ->
                            val updated = list.filterNot { it.device.address == state.device.device.address }
                            updated + state.device
                        }
                    }

                    is BluetoothScannerState.OnListDeviceReceived -> {
                        if (state.deviceList.isNotEmpty()) _availableDevices.update { it + state.deviceList }
                    }

                }
            }
            .launchIn(scope)
    }

    override suspend fun connect(address: String) {
        try {
            reconnectionJob?.cancel()
            reconnectionJob = null

            val fromCache: BluetoothDevice? = _availableDevices.value.find { it.device.address == address }?.device
            val device = fromCache ?: run {
                val adapter = manager.adapter
                adapter?.getRemoteDevice(address)
            }

            requireNotNull(device) {
                "Device: $device not found"
            }

            lastDevice = device

            connectionJob = scope.launch(Dispatchers.IO) {
                connectGatt {
                    connectedDevice.value = if (it) {
                        address
                    } else {
                        null
                    }
                    isUserConnected = it
                }
            }
        } catch (_: Throwable) {
            connectedDevice.value = null
        }
    }

    override fun disconnect() {
        isUserConnected = false
        reconnectionJob?.cancel()
        reconnectionJob = null
        connectionJob?.cancel()
        connectionJob = null
        stopScan()
        bluetoothConnector.safeCloseGatt()
    }

    override fun startScan() {
        isUserConnected = true
        bluetoothScanner.startScan(filter, settings)
    }

    override fun stopScan() {
        bluetoothScanner.stopScan()
    }

    private fun startReconnectionAttempts(address: String) {
        if (!isUserConnected) return
        reconnectionJob?.cancel()
        reconnectionJob = scope.launch(Dispatchers.Main.immediate) {
            connectGatt { connectedDevice.value = address }
        }
    }

    private fun connectGatt(connected: (Boolean) -> Unit) {
        bluetoothConnector.safeCloseGatt()
        val device = lastDevice ?: return
        bluetoothConnector.connect(device = device).onEach { state ->
            when (state) {
                is HeartRateConnectionState.HeartRate -> {
                    _heartRateFlow.value = state.pulse
                }

                is HeartRateConnectionState.IsConnected -> {
                    connected.invoke(state.isConnected)
                }

                is HeartRateConnectionState.Disconnected -> {
                    startReconnectionAttempts(device.address)
                }
            }
        }.launchIn(scope)
    }

    companion object {
        internal val heartRateMeasurementUUID = UUID.fromString("00002A37-0000-1000-8000-00805f9b34fb")
        internal val cccdUUID = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")
        internal val heartRateServiceUUID = UUID.fromString("0000180D-0000-1000-8000-00805f9b34fb")
    }
}