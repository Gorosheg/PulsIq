package gorosheg.pulsiq.statistics.main.presentation.model

import gorosheg.pulsiq.common.model.PulseStatistic

internal data class StatisticsState(
    val pulseStatisticList: List<PulseStatisticGroup> = emptyList(),
)

internal data class PulseStatisticGroup(
    val title: String,
    val items: List<PulseStatistic>
)
