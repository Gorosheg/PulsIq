package gorosheg.pulsiq.statistics.tracking_session.ui.model

internal data class TrackingSessionUiState(
    val name: String,
    val dateStart: String,
    val dateEnd: String,
    val pulse: List<Pair<Int, Long>>,
    val highestPulse: Int,
    val lowestPulse: Int,
    val averagePulse: Int,
    val isEditDialogShow: Boolean = false,
)