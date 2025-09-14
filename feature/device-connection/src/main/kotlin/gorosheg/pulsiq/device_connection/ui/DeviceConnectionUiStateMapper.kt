package gorosheg.pulsiq.device_connection.ui

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState

internal class DeviceConnectionUiStateMapper : UiStateMapper<DeviceConnectionState, DeviceConnectionUiState> {

    override fun DeviceConnectionState.map(): DeviceConnectionUiState {
        return DeviceConnectionUiState(
            a = a
        )
    }
}