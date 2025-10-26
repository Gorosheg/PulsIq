package gorosheg.pulsiq.statistics.main.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsEffect
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.main.ui.StatisticsUiStateMapper
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics_repository.StatisticsRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
) : BaseViewModel<StatisticsState, StatisticsUiState, StatisticsEffect>(
    initState = StatisticsState(),
    uiStateMapper = StatisticsUiStateMapper()
) {

    init {
        getStatistics()
    }

    private fun getStatistics() {
        statisticsRepository.getPulse()
            .onEach { pulseStatistic ->
                updateState { copy(pulseStatisticList = pulseStatistic) }
            }
            .launchIn(viewModelScope)
    }
}