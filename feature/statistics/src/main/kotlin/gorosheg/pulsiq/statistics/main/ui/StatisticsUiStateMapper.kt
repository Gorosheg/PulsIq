package gorosheg.pulsiq.statistics.main.ui

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.main.presentation.model.PulseStatisticGroup
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatistic
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatisticGroup
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class StatisticsUiStateMapper : UiStateMapper<StatisticsState, StatisticsUiState> {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM. HH:mm")

    override fun StatisticsState.map(): StatisticsUiState {
        return StatisticsUiState(
            pulseStatisticList = pulseStatisticList.toUiGroups()
        )
    }

    private fun List<PulseStatisticGroup>.toUiGroups(): List<UiPulseStatisticGroup> {
        return this.map { group ->
            UiPulseStatisticGroup(
                title = group.title,
                items = group.items.toUiPulseStatistic()
            )
        }
    }

    private fun List<PulseStatistic>.toUiPulseStatistic(): List<UiPulseStatistic> {
        return this.map { pulseStatistic ->
            UiPulseStatistic(
                id = pulseStatistic.id,
                name = pulseStatistic.name,
                dateStart = formatDate(pulseStatistic.dateStart),
            )
        }
    }

    private fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }
}