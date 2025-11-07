package gorosheg.pulsiq.statistics.tracking_session.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.TrackingSessionUiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import gorosheg.pulsiq.statistics_repository.StatisticsRepository
import kotlinx.coroutines.launch

internal class TrackingSessionViewModel(
    private val statisticsRepository: StatisticsRepository,
) : BaseViewModel<TrackingSessionState, TrackingSessionUiState>(
    initState = TrackingSessionState(),
    uiStateMapper = TrackingSessionUiStateMapper()
) {

    fun getTrackingSession(id: Int) {
        viewModelScope.launch {
            val trackingSession: PulseStatistic? = statisticsRepository.getPulse(id)
            trackingSession ?: return@launch
            updateState {
                copy(
                    id = trackingSession.id,
                    dateStart = trackingSession.dateStart,
                    dateEnd = trackingSession.dateEnd ?: 0,
                    name = trackingSession.name,
                    pulse = trackingSession.pulse,
                )
            }
        }
    }

    fun onEditClick() {
        updateState { copy(isEditDialogShow = true) }
    }

    fun closeEditDialog() {
        updateState { copy(isEditDialogShow = false) }
    }

    fun changeName(name: String) {
        viewModelScope.launch {
            statisticsRepository.changeStatisticsSessionName(id = getState.id, name = name)
        }
        updateState { copy(name = name) }
    }
}