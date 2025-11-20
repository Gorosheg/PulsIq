package gorosheg.pulsiq.statistics.tracking_session.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.StatisticsRepository
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.mapper.TrackingSessionUiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import kotlinx.coroutines.launch

internal class TrackingSessionViewModel(
    private val statisticsRepository: StatisticsRepository,
    uiStateMapper: TrackingSessionUiStateMapper,
) : BaseViewModel<TrackingSessionState, TrackingSessionUiState>(
    initState = TrackingSessionState(),
    uiStateMapper = uiStateMapper
) {

    fun getTrackingSession(id: Int) {
        viewModelScope.launch {
            val trackingSession: PulseStatistic? = statisticsRepository.getPulse(id)
            trackingSession ?: return@launch
            updateState {
                copy(
                    id = trackingSession.id,
                    dateStart = trackingSession.dateStart,
                    dateEnd = (trackingSession.dateEnd ?: trackingSession.dateStart),
                    name = trackingSession.name,
                    pulse = trackingSession.pulse.map { it.first to it.second },
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