package gorosheg.pulsiq.device_connection.ui.mapper

import gorosheg.pulsiq.bluetooth.model.DomainBluetoothDevice
import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.device_connection.R
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.presentation.model.ErrorType
import gorosheg.pulsiq.device_connection.ui.component.device_list.UiBluetoothDevice
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.ScanningButtonsState

internal class DeviceConnectionUiStateMapper : UiStateMapper<DeviceConnectionState, DeviceConnectionUiState> {

    override fun DeviceConnectionState.map(): DeviceConnectionUiState {
        return DeviceConnectionUiState(
            devices = devices.toUiBluetoothDevices(
                connectedAddress = connectedAddress ?: "",
                connectingAddress = connectingAddress ?: ""
            ),
            error = error.buildErrorText(),
            scanningButtonsState = ScanningButtonsState(isScanning = isScanning)
        )
    }

    private fun ErrorType?.buildErrorText(): Int? {
        return when (this) {
            ErrorType.NO_CONNECTING -> R.string.connectionError
            null -> null
        }
    }

    private fun List<DomainBluetoothDevice>.toUiBluetoothDevices(
        connectedAddress: String,
        connectingAddress: String
    ): List<UiBluetoothDevice> {
        return map {
            UiBluetoothDevice(
                name = it.name,
                address = it.device.address,
                connectingState = it.device.address.buildConnectingState(
                    connectedAddress = connectedAddress,
                    connectingAddress = connectingAddress
                )
            )
        }
    }

    private fun String.buildConnectingState(
        connectedAddress: String,
        connectingAddress: String
    ): UiBluetoothDevice.ConnectingState {
        return when {
            connectedAddress == this -> UiBluetoothDevice.ConnectingState.CONNECTED
            connectingAddress == this -> UiBluetoothDevice.ConnectingState.CONNECTING
            else -> UiBluetoothDevice.ConnectingState.NOT_CONNECTED
        }
    }
}