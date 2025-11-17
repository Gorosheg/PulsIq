package gorosheg.pulsiq.statistics.tracking_session.ui.model

internal data class TrackingSessionUiState(
    val trackingSessionHeaderState: TrackingSessionHeaderState,
    val pulseSummaryState: PulseSummaryState,
    val chartState: ChartState? = null,
    val isEditDialogShow: Boolean = false
)

internal data class TrackingSessionHeaderState(
    val name: String,
    val timeStart: String,
    val timeEnd: String,
    val date: String,
)

internal data class ChartState(
    val timeList: List<Number> = emptyList(),
    val pulseList: List<Number> = emptyList(),
    val chartPadding: Double = 3.0,
)

internal data class PulseSummaryState(
    val highestPulse: String,
    val lowestPulse: String,
    val averagePulse: String,
)