package gorosheg.pulsiq.statistics.tracking_session.presentation.model

import java.time.LocalDateTime

internal data class TrackingSessionState(
    val id: Int = -1,
    val dateStart: LocalDateTime = LocalDateTime.now(),
    val dateEnd: LocalDateTime = LocalDateTime.now(),
    val name: String = "",
    val pulse: List<Pair<Int, LocalDateTime>> = emptyList(),
    val isEditDialogShow: Boolean = false,
)