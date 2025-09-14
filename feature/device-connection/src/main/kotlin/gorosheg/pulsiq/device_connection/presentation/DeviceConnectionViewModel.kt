package gorosheg.pulsiq.device_connection.presentation

import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionEffect
import gorosheg.pulsiq.device_connection.presentation.model.DeviceConnectionState
import gorosheg.pulsiq.device_connection.ui.DeviceConnectionUiStateMapper
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState

internal class DeviceConnectionViewModel : BaseViewModel<DeviceConnectionState, DeviceConnectionUiState, DeviceConnectionEffect>(
    initState = DeviceConnectionState(),
    uiStateMapper = DeviceConnectionUiStateMapper()
) {

}