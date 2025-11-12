package gorosheg.pulsiq.statistics.tracking_session.ui.mapper

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.ChartState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.PulseSummaryState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionHeaderState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class TrackingSessionUiStateMapper : UiStateMapper<TrackingSessionState, TrackingSessionUiState> {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM. HH:mm")

    override fun TrackingSessionState.map(): TrackingSessionUiState {
        return TrackingSessionUiState(
            trackingSessionHeaderState = buildTrackingSessionHeaderState(dateStart, dateEnd, name),
            pulseSummaryState = buildPulseSummaryState(pulse),
            chartState = buildChartState(pulse),
            isEditDialogShow = isEditDialogShow
        )
    }

    private fun buildTrackingSessionHeaderState(dateStart: Long, dateEnd: Long, name: String): TrackingSessionHeaderState{
        return TrackingSessionHeaderState(
            dateStart = formatDate(dateStart),
            dateEnd = formatDate(dateEnd),
            name = name,
        )
    }

    private fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }

    private fun buildPulseSummaryState(pulse: List<Pair<Int, Long>>): PulseSummaryState {
        val pulseValues = pulse.map { it.first }
        val average =
            if (pulseValues.isNotEmpty()) pulseValues.average().toInt()
            else 0

        return PulseSummaryState(
            highestPulse = pulseValues.maxOrNull() ?: 0,
            lowestPulse = pulseValues.minOrNull() ?: 0,
            averagePulse = average,
        )
    }

    private fun buildChartState(pulse: List<Pair<Int, Long>>): ChartState? {
        if (pulse.isEmpty()) return null
        return ChartState(
            timeList = pulse.toTimeList(),
            pulseList = pulse.toPulseList(),
        )
    }

    private fun List<Pair<Int, Long>>.toTimeList(): List<Number> {
        if (this.isEmpty()) return emptyList()
        val firstTs = this.first().second
        return this.map { ((it.second - firstTs).coerceAtLeast(0L) / 1000.0) }
    }

    private fun List<Pair<Int, Long>>.toPulseList(): List<Number> {
        if (this.isEmpty()) return emptyList()
        return this.map { it.first.toDouble() }
    }
}