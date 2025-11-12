package gorosheg.pulsiq.statistics.main.presentation

import android.content.Context
import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.statistics.R
import gorosheg.pulsiq.statistics.main.presentation.model.PulseStatisticGroup
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.TextStyle
import java.util.Locale

internal fun buildGroups(list: List<PulseStatistic>, context: Context): List<PulseStatisticGroup> {
    val ruLocale = Locale("ru")
    val zone: ZoneId = ZoneId.systemDefault()
    val today = LocalDate.now(zone)
    val yesterday = today.minusDays(1)

    val (todayList, notToday) = list.partition { it.localDate(zone) == today }
    val (yesterdayList, rest) = notToday.partition { it.localDate(zone) == yesterday }

    val currentYear = today.year
    val (currentYearList, previousYearsList) = rest.partition { it.localDate(zone).year == currentYear }

    val monthMap = currentYearList.groupBy { it.localDate(zone).monthValue }
    val monthsDesc = monthMap.keys.sortedDescending()

    val yearMap = previousYearsList.groupBy { it.localDate(zone).year }
    val yearsDesc = yearMap.keys.sortedDescending()

    val result = mutableListOf<PulseStatisticGroup>()
    if (todayList.isNotEmpty()) {
        result.add(
            PulseStatisticGroup(
                title = context.getString(R.string.today),
                items = todayList.sortedByDescending { it.dateStart }
            )
        )
    }
    if (yesterdayList.isNotEmpty()) {
        result.add(
            PulseStatisticGroup(
                title = context.getString(R.string.yesterday),
                items = yesterdayList.sortedByDescending { it.dateStart }
            )
        )
    }
    monthsDesc.forEach { month ->
        val items = monthMap[month].orEmpty().sortedByDescending { it.dateStart }
        if (items.isNotEmpty()) {
            val monthName = LocalDate.of(currentYear, month, 1)
                .month
                .getDisplayName(TextStyle.FULL_STANDALONE, ruLocale)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(ruLocale) else it.toString() }
            result.add(PulseStatisticGroup(title = monthName, items = items))
        }
    }
    yearsDesc.forEach { year ->
        val items = yearMap[year].orEmpty().sortedByDescending { it.dateStart }
        if (items.isNotEmpty()) {
            result.add(PulseStatisticGroup(title = year.toString(), items = items))
        }
    }
    return result
}

private fun PulseStatistic.localDate(zone: ZoneId): LocalDate =
    Instant.ofEpochMilli(dateStart).atZone(zone).toLocalDate()