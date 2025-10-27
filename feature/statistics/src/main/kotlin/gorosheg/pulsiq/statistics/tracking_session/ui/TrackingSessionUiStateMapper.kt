package gorosheg.pulsiq.statistics.tracking_session.ui

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState

internal class TrackingSessionUiStateMapper : UiStateMapper<TrackingSessionState, TrackingSessionUiState> {
    override fun TrackingSessionState.map(): TrackingSessionUiState {
        return TrackingSessionUiState()
    }
}