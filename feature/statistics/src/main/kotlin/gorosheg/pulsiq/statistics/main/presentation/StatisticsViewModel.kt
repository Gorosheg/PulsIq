package gorosheg.pulsiq.statistics.main.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.navigation.NavigatorHolder
import gorosheg.pulsiq.common.navigation.provider.TrackingSessionScreenProvider
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.StatisticsRepository
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.main.ui.mapper.StatisticsUiStateMapper
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
    private val navigator: NavigatorHolder,
    private val trackingSessionScreenProvider: TrackingSessionScreenProvider,
    uiStateMapper: StatisticsUiStateMapper,
) : BaseViewModel<StatisticsState, StatisticsUiState>(
    initState = StatisticsState(),
    uiStateMapper = uiStateMapper
) {

    init {
        subscribeToStatistics()
    }

    fun removeTrackingSession(id: Int) {
        viewModelScope.launch {
            statisticsRepository.deletePulseStatistic(id)
        }
    }

    fun navigateToTrackingSession(id: Int) {
        navigator.navigator?.push(trackingSessionScreenProvider(trackingSessionId = id))
    }

    private fun subscribeToStatistics() {
        statisticsRepository.getPulse()
            .onEach { list ->
                updateState { copy(pulseStatisticList = list) }
            }
            .launchIn(viewModelScope)
    }
}