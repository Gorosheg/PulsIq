package gorosheg.pulsiq.statistics.main.ui

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.main.presentation.model.StatisticsState
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatistic
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class StatisticsUiStateMapper : UiStateMapper<StatisticsState, StatisticsUiState> {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM. HH:mm")

    override fun StatisticsState.map(): StatisticsUiState {
        return StatisticsUiState(
            pulseStatisticList = pulseStatisticList.toUiPulseStatistic()
        )
    }

    private fun List<PulseStatistic>.toUiPulseStatistic(): List<UiPulseStatistic> {
        return this.map { pulseStatistic ->
            val pulse = pulseStatistic.pulse
            val highest = pulse.maxOrNull() ?: 0
            val lowest = pulse.minOrNull() ?: 0
            val average = if (pulse.isNotEmpty()) pulse.average().toInt() else 0

            UiPulseStatistic(
                id = pulseStatistic.id,
                dateStart = formatDate(pulseStatistic.dateStart),
                dateEnd = pulseStatistic.dateEnd?.let { formatDate(it) } ?: "",
                name = pulseStatistic.name,
                pulse = pulse,
                highestPulse = highest,
                lowestPulse = lowest,
                averagePulse = average,
            )
        }
    }

    private fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }
}