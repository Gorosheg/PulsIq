package gorosheg.pulsiq.statistics.tracking_session.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.statistics.tracking_session.ui.components.PulseDynamics
import gorosheg.pulsiq.statistics.tracking_session.ui.components.PulseSummary
import gorosheg.pulsiq.statistics.tracking_session.ui.components.TrackingSessionHeader
import gorosheg.pulsiq.statistics.tracking_session.ui.model.ChartState
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState
import gorosheg.pulsiq.ui.EditNameDialog

@Composable
internal fun TrackingSessionScreenContent(
    state: TrackingSessionUiState,
    onEditClick: () -> Unit,
    onCloseEditDialogClick: () -> Unit,
    onNameChanged: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        TrackingSessionHeader(
            state = state.trackingSessionHeaderState,
            onEditClick = onEditClick
        )
        PulseSummary(state = state.pulseSummaryState)
        PulseDynamics(chartState = state.chartState)
    }

    if (state.isEditDialogShow) {
        EditNameDialog(
            name = state.trackingSessionHeaderState.name,
            onNameChanged = onNameChanged,
            onCloseEditDialogClick = onCloseEditDialogClick
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "TrackingSessionScreenContent Preview")
@Composable
private fun TrackingSessionScreenContentPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        val pulseValues: List<Int> = listOf(72, 78, 85, 92, 110, 105, 98, 88, 95, 102, 96, 90)
        val timeSeconds: List<Int> = pulseValues.indices.map { it * 60 }
        TrackingSessionScreenContent(
            state = TrackingSessionUiState(
                trackingSessionHeaderState = gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionHeaderState(
                    name = "Утренняя тренировка",
                    dateStart = "26.10. 10:00",
                    dateEnd = "26.10. 11:00",
                ),
                pulseSummaryState = gorosheg.pulsiq.statistics.tracking_session.ui.model.PulseSummaryState(
                    highestPulse = pulseValues.maxOrNull() ?: 0,
                    lowestPulse = pulseValues.minOrNull() ?: 0,
                    averagePulse = pulseValues.average().toInt(),
                ),
                chartState = ChartState(
                    timeList = timeSeconds,
                    pulseList = pulseValues
                ),
                isEditDialogShow = false
            ),
            onEditClick = {},
            onCloseEditDialogClick = {},
            onNameChanged = {},
        )
    }
}
