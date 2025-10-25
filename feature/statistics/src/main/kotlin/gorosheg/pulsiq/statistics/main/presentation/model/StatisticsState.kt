package gorosheg.pulsiq.statistics.main.presentation.model

import gorosheg.pulsiq.common.model.PulseStatistic

internal data class StatisticsState(
    val pulseStatisticList: List<PulseStatistic> = emptyList(),
)