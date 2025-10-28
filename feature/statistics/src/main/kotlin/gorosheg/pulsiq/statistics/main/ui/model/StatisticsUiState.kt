package gorosheg.pulsiq.statistics.main.ui.model

internal data class StatisticsUiState(
    val pulseStatisticList: List<UiPulseStatistic> = emptyList(),
)

internal data class UiPulseStatistic(
    val id: Int,
    val name: String,
    val dateStart: String,
)