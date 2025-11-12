package gorosheg.pulsiq.statistics.tracking_session.ui.model

internal data class TrackingSessionUiState(
    val trackingSessionHeaderState: TrackingSessionHeaderState,
    val pulseSummaryState: PulseSummaryState,
    val chartState: ChartState? = null,
    val isEditDialogShow: Boolean = false
)

internal data class TrackingSessionHeaderState(
    val name: String,
    val dateStart: String,
    val dateEnd: String,
)

internal data class ChartState(
    val timeList: List<Number> = emptyList(),
    val pulseList: List<Number> = emptyList(),
    val chartPadding: Double = 3.0,
)

internal data class PulseSummaryState(
    val highestPulse: Int,
    val lowestPulse: Int,
    val averagePulse: Int,
)