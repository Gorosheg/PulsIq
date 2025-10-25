package gorosheg.pulsiq.statistics.main.ui.model

internal data class StatisticsUiState(
    val pulseStatisticList: List<UiPulseStatistic> = emptyList(),
)

internal data class UiPulseStatistic(
    val id: Int,
    val dateStart: String,
    val dateEnd: String,
    val name: String,
    val pulse: List<Int>,
    val highestPulse: Int,
    val lowestPulse: Int,
    val averagePulse: Int,
)