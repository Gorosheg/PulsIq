package gorosheg.pulsiq.statistics.main.presentation

import android.content.Context
import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.navigation.NavigatorHolder
import gorosheg.pulsiq.common.navigation.provider.TrackingSessionScreenProvider
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.main.ui.mapper.StatisticsUiStateMapper
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics_repository.StatisticsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
    private val navigator: NavigatorHolder,
    private val trackingSessionScreenProvider: TrackingSessionScreenProvider,
    private val context: Context,
    uiStateMapper: StatisticsUiStateMapper,
) : BaseViewModel<StatisticsState, StatisticsUiState>(
    initState = StatisticsState(),
    uiStateMapper = uiStateMapper
) {

    init {
        getStatistics()
    }

    fun removeTrackingSession(id: Int) {
        viewModelScope.launch {
            statisticsRepository.deletePulseStatistic(id)
        }
    }

    fun navigateToTrackingSession(id: Int) {
        navigator.navigator?.push(trackingSessionScreenProvider(trackingSessionId = id))
    }

    private fun getStatistics() {
        statisticsRepository.getPulse()
            .onEach { list ->
                val groups = buildGroups(list, context)
                updateState { copy(pulseStatisticList = groups) }
            }
            .launchIn(viewModelScope)
    }
}