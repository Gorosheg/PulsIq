package gorosheg.pulsiq.statistics.ui

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.ui.model.StatisticsUiState

internal class StatisticsUiStateMapper : UiStateMapper<StatisticsState, StatisticsUiState> {

    override fun StatisticsState.map(): StatisticsUiState {
        return StatisticsUiState(
            pulse = pulse
        )
    }
}