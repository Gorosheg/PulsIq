package gorosheg.pulsiq.statistics.tracking_session.ui.mapper

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.ChartState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.PulseSummaryState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionHeaderState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

internal class TrackingSessionUiStateMapper : UiStateMapper<TrackingSessionState, TrackingSessionUiState> {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    override fun TrackingSessionState.map(): TrackingSessionUiState {
        return TrackingSessionUiState(
            trackingSessionHeaderState = buildTrackingSessionHeaderState(dateStart, dateEnd, name),
            pulseSummaryState = buildPulseSummaryState(pulse),
            chartState = buildChartState(pulse),
            isEditDialogShow = isEditDialogShow
        )
    }

    private fun buildTrackingSessionHeaderState(
        dateStart: LocalDateTime,
        dateEnd: LocalDateTime,
        name: String
    ): TrackingSessionHeaderState {
        return TrackingSessionHeaderState(
            name = name,
            timeStart = dateStart.format(timeFormatter),
            timeEnd = dateEnd.format(timeFormatter),
            date = dateStart.format(dateFormatter),
        )
    }

    private fun buildPulseSummaryState(pulse: List<Pair<Int, LocalDateTime>>): PulseSummaryState {
        val pulseValues = pulse.map { it.first }
        val average =
            if (pulseValues.isNotEmpty()) pulseValues.average().toInt().toString()
            else 0.toString()

        return PulseSummaryState(
            highestPulse = (pulseValues.maxOrNull() ?: 0).toString(),
            lowestPulse = (pulseValues.minOrNull() ?: 0).toString(),
            averagePulse = average,
        )
    }

    private fun buildChartState(pulse: List<Pair<Int, LocalDateTime>>): ChartState? {
        if (pulse.isEmpty()) return null
        return ChartState(
            timeList = pulse.toTimeList(),
            pulseList = pulse.toPulseList(),
        )
    }

    private fun List<Pair<Int, LocalDateTime>>.toTimeList(): List<Number> {
        if (this.isEmpty()) return emptyList()
        val start = this.first().second
        return this.map { pair ->
            Duration.between(start, pair.second).seconds.toInt()
        }
    }

    private fun List<Pair<Int, LocalDateTime>>.toPulseList(): List<Number> {
        if (this.isEmpty()) return emptyList()
        return this.map { it.first.toDouble() }
    }
}