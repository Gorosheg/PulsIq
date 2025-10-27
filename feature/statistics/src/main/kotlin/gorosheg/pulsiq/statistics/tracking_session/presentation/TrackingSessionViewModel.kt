package gorosheg.pulsiq.statistics.tracking_session.presentation

import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionEffect
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.TrackingSessionUiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState

internal class TrackingSessionViewModel(

) : BaseViewModel<TrackingSessionState, TrackingSessionUiState, TrackingSessionEffect>(
    initState = TrackingSessionState(),
    uiStateMapper = TrackingSessionUiStateMapper()
) {

}