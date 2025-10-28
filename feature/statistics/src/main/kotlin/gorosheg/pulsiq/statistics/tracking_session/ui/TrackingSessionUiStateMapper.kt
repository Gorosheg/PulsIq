package gorosheg.pulsiq.statistics.tracking_session.ui

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.statistics.tracking_session.presentation.model.TrackingSessionState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal class TrackingSessionUiStateMapper : UiStateMapper<TrackingSessionState, TrackingSessionUiState> {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM. HH:mm")

    override fun TrackingSessionState.map(): TrackingSessionUiState {
        val pulse = pulse.drop(1)
        val average =
            if (pulse.isNotEmpty()) pulse.average().toInt()
            else 0

        return TrackingSessionUiState(
            dateStart = formatDate(this.dateStart),
            dateEnd = formatDate(dateEnd),
            name = name,
            pulse = pulse,
            highestPulse = pulse.maxOrNull() ?: 0,
            lowestPulse = pulse.minOrNull() ?: 0,
            averagePulse = average,
        )
    }

    private fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }
}