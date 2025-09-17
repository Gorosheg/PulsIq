package gorosheg.pulsiq.device_connection.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.bluetooth.model.BleDevice
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionEffect
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.ui.DeviceConnectionUiStateMapper
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.UiBleDevice
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class DeviceConnectionViewModel(
    private val heartBeatDataSource: HeartBeatDataSource,
) : BaseViewModel<DeviceConnectionState, DeviceConnectionUiState, DeviceConnectionEffect>(
    initState = DeviceConnectionState(),
    uiStateMapper = DeviceConnectionUiStateMapper()
) {

    init {
        viewModelScope.launch {
            heartBeatDataSource.subscribeAvailableDevicesFlow()
                .onEach {
                    state { copy(devices = it.toUi(), error = null) }
                }
                .launchIn(viewModelScope)
        }
    }

    fun startScan() {
        heartBeatDataSource.startScan()
        state { copy(isScanning = true, error = null) }
    }

    fun stopScan() {
        heartBeatDataSource.stopScan()
        state { copy(isScanning = false, error = null) }
    }

    fun onDeviceClick(address: String) {
        state { copy(connectingAddress = address, error = null) }
        viewModelScope.launch {
            heartBeatDataSource.connect(address) { isConnected ->
                if (isConnected) {
                    state { copy(connectedAddress = address) }
                } else {
                    state { copy(connectingAddress = null, error = "Ошибка подключения") }
                }
            }
        }
    }

    fun disconnect() {
        heartBeatDataSource.disconnect()
        state { copy(connectedAddress = null, connectingAddress = null, error = null) }
    }

    private fun List<BleDevice>.toUi(): List<UiBleDevice> {
        return map {
            UiBleDevice(
                name = it.name,
                address = it.device.address,
            )
        }
    }
}