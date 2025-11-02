package gorosheg.pulsiq.common.model

data class PulseStatistic(
    val id: Int,
    val dateStart: Long,
    val dateEnd: Long?,
    val name: String,
    val pulse: List<Pair<Int, Long>>,
)