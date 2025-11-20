package gorosheg.pulsiq.common.model

import java.time.LocalDateTime

data class PulseStatistic(
    val id: Int,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime?,
    val name: String,
    val pulse: List<Pair<Int, LocalDateTime>>,
)