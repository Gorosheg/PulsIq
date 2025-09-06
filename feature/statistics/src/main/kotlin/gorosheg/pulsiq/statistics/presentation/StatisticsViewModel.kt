package gorosheg.pulsiq.statistics.presentation

import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.statistics.presentation.model.StatisticsEffect
import gorosheg.pulsiq.statistics.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.ui.StatisticsUiStateMapper
import gorosheg.pulsiq.statistics.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics_repository.StatisticsRepository

internal class StatisticsViewModel(
    private val statisticsRepository: StatisticsRepository,
) : BaseViewModel<StatisticsState, StatisticsUiState, StatisticsEffect>(
    initState = StatisticsState(),
    uiStateMapper = StatisticsUiStateMapper()
) {

}