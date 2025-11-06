package gorosheg.pulsiq.statistics.main.ui.model

internal data class StatisticsUiState(
    val pulseStatisticList: List<UiPulseStatisticGroup> = emptyList(),
)

internal data class UiPulseStatisticGroup(
    val title: String,
    val items: List<UiPulseStatistic>,
)

internal data class UiPulseStatistic(
    val id: Int,
    val name: String,
    val dateStart: String,
)