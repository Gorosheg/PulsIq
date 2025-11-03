package gorosheg.pulsiq.device_connection.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.presentation.model.ErrorType
import gorosheg.pulsiq.device_connection.ui.DeviceConnectionUiStateMapper
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class DeviceConnectionViewModel(
    private val bluetoothRepository: BluetoothRepository,
) : BaseViewModel<DeviceConnectionState, DeviceConnectionUiState>(
    initState = DeviceConnectionState(),
    uiStateMapper = DeviceConnectionUiStateMapper()
) {

    init {
        bluetoothRepository.availableDevices
            .onEach {
                updateState { copy(devices = it, error = null) }
            }
            .launchIn(viewModelScope)

        bluetoothRepository.connectedDevice
            .onEach {
                if (it != null) {
                    updateState { copy(connectedAddress = it) }
                } else {
                    updateState { copy(connectingAddress = null, error = ErrorType.NO_CONNECTING) }
                }
            }
            .launchIn(viewModelScope)
    }

    fun startScan() {
        bluetoothRepository.startScan()
        updateState { copy(isScanning = true, error = null) }
    }

    fun stopScan() {
        bluetoothRepository.stopScan()
        updateState { copy(isScanning = false, error = null) }
    }

    fun onDeviceClick(address: String) {
        updateState { copy(connectingAddress = address, error = null) }
        viewModelScope.launch {
            bluetoothRepository.connect(address)
        }
    }

    fun disconnect() {
        bluetoothRepository.disconnect()
        updateState { copy(connectedAddress = null, connectingAddress = null, error = null) }
    }
}