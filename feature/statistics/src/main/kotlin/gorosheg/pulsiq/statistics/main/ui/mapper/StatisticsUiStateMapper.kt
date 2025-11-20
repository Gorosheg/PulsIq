package gorosheg.pulsiq.statistics.main.ui.mapper

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState

internal class StatisticsUiStateMapper(
    private val uiPulseStatisticGroupBuilder: UiPulseStatisticGroupBuilder
) : UiStateMapper<StatisticsState, StatisticsUiState> {

    override fun StatisticsState.map(): StatisticsUiState {
        return StatisticsUiState(
            pulseStatisticList = uiPulseStatisticGroupBuilder.buildUiGroups(pulseStatisticList)
        )
    }
}