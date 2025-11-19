package gorosheg.pulsiq.device_connection.ui.mapper

import gorosheg.pulsiq.bluetooth.model.DomainBluetoothDevice
import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.device_connection.R
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.presentation.model.ErrorType
import gorosheg.pulsiq.device_connection.ui.model.ConnectingState
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.UiBluetoothDevice

internal class DeviceConnectionUiStateMapper : UiStateMapper<DeviceConnectionState, DeviceConnectionUiState> {

    override fun DeviceConnectionState.map(): DeviceConnectionUiState {
        return DeviceConnectionUiState(
            isScanning = isScanning,
            devices = devices.toUiBluetoothDevices(
                connectedAddress = connectedAddress ?: "",
                connectingAddress = connectingAddress ?: ""
            ),
            error = error.buildErrorText(),
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
                name = it.name.ifBlank { "Безымянное устройство" },
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
    ): ConnectingState {
        return when {
            connectedAddress == this -> ConnectingState.CONNECTED
            connectingAddress == this -> ConnectingState.CONNECTING
            else -> ConnectingState.NOT_CONNECTED
        }
    }
}