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
        val pulsePairs = this.pulse.drop(1)
        val pulseValues = pulsePairs.map { it.first }
        val average =
            if (pulseValues.isNotEmpty()) pulseValues.average().toInt()
            else 0

        return TrackingSessionUiState(
            dateStart = formatDate(this.dateStart),
            dateEnd = formatDate(this.dateEnd),
            name = this.name,
            pulse = pulsePairs,
            highestPulse = pulseValues.maxOrNull() ?: 0,
            lowestPulse = pulseValues.minOrNull() ?: 0,
            averagePulse = average,
            isEditDialogShow = isEditDialogShow
        )
    }

    private fun formatDate(epochMillis: Long): String {
        return Instant.ofEpochMilli(epochMillis)
            .atZone(ZoneId.systemDefault())
            .format(dateFormatter)
    }
}