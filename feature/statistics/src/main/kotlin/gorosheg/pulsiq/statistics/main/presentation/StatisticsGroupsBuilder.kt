package gorosheg.pulsiq.statistics.main.presentation

import android.content.Context
import gorosheg.pulsiq.common.model.PulseStatistic

@Deprecated("No longer used. Grouping moved to StatisticsUiStateMapper. This function will be removed in future versions.")
internal fun buildGroups(list: List<PulseStatistic>, context: Context): List<PulseStatistic> {
    return list
}