package gorosheg.pulsiq.statistics.tracking_session.presentation.model

internal data class TrackingSessionState(
    val id: Int = -1,
    val dateStart: Long = 0,
    val dateEnd: Long = 0,
    val name: String = "",
    val pulse: List<Pair<Int, Long>> = emptyList(),
    val isEditDialogShow: Boolean = false,
)