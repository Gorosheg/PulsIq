package gorosheg.pulsiq.statistics.main.ui.mapper

import android.content.Context
import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.statistics.R
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatistic
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatisticGroup
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

internal class UiPulseStatisticGroupBuilder(
    private val context: Context,
    private val ruLocale: Locale,
    private val todayProvider: () -> LocalDate = { LocalDate.now() },
) {

    private val dateFormatter = DateTimeFormatter.ofPattern("dd.MM")

    internal fun buildUiGroups(listStatistics: List<PulseStatistic>): List<UiPulseStatisticGroup> {
        val parts = listStatistics.partitionForUi()

        return buildList {
            buildSimpleGroup(
                title = context.getString(R.string.today),
                items = parts.today
            )?.let(::add)

            buildSimpleGroup(
                title = context.getString(R.string.yesterday),
                items = parts.yesterday
            )?.let(::add)

            addAll(
                buildMonthGroups(
                    currentYearList = parts.currentYearList,
                    currentYear = parts.currentYear
                )
            )

            addAll(buildYearGroups(previousYearsList = parts.previousYearsList))
        }
    }


    private fun List<PulseStatistic>.partitionForUi(): PartitionResult {
        val todayDate = todayProvider()
        val yesterdayDate = todayDate.minusDays(1)
        val currentYear = todayDate.year

        val (todayList, notToday) = this.partition { it.dateStart.toLocalDate() == todayDate }
        val (yesterdayList, olderDates) = notToday.partition { it.dateStart.toLocalDate() == yesterdayDate }
        val (currentYearList, previousYearsList) = olderDates.partition { it.dateStart.year == currentYear }

        return PartitionResult(
            today = todayList,
            yesterday = yesterdayList,
            currentYearList = currentYearList,
            previousYearsList = previousYearsList,
            currentYear = currentYear,
        )
    }

    private fun buildSimpleGroup(title: String, items: List<PulseStatistic>): UiPulseStatisticGroup? {
        if (items.isEmpty()) return null
        return UiPulseStatisticGroup(
            title = title,
            items = items.sortedDescByDate().toUiPulseStatistic()
        )
    }

    private fun buildMonthGroups(currentYearList: List<PulseStatistic>, currentYear: Int): List<UiPulseStatisticGroup> {
        if (currentYearList.isEmpty()) return emptyList()
        val monthMap = currentYearList.groupBy { it.dateStart.monthValue }
        val monthsDesc = monthMap.keys.sortedDescending()

        return monthsDesc.mapNotNull { month ->
            val items = monthMap[month].orEmpty().sortedDescByDate()
            if (items.isEmpty()) null
            else UiPulseStatisticGroup(
                title = monthName(currentYear, month),
                items = items.toUiPulseStatistic()
            )
        }
    }

    private fun buildYearGroups(previousYearsList: List<PulseStatistic>): List<UiPulseStatisticGroup> {
        if (previousYearsList.isEmpty()) return emptyList()
        val yearMap = previousYearsList.groupBy { it.dateStart.year }
        val yearsDesc = yearMap.keys.sortedDescending()

        return yearsDesc.mapNotNull { year ->
            val items = yearMap[year].orEmpty().sortedDescByDate()
            if (items.isEmpty()) null
            else UiPulseStatisticGroup(
                title = year.toString(),
                items = items.toUiPulseStatistic()
            )
        }
    }

    private fun monthName(currentYear: Int, month: Int): String {
        val name = LocalDate.of(currentYear, month, 1)
            .month
            .getDisplayName(TextStyle.FULL_STANDALONE, ruLocale)
        return name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(ruLocale)
            else it.toString()
        }
    }

    private fun List<PulseStatistic>.sortedDescByDate(): List<PulseStatistic> {
        return sortedByDescending { it.dateStart }
    }

    private fun List<PulseStatistic>.toUiPulseStatistic(): List<UiPulseStatistic> {
        return map { pulseStatistic ->
            UiPulseStatistic(
                id = pulseStatistic.id,
                name = pulseStatistic.name,
                dateStart = formatDate(pulseStatistic.dateStart),
            )
        }
    }

    private fun formatDate(dateTime: LocalDateTime): String {
        return dateTime.toLocalDate().format(dateFormatter)
    }

    private data class PartitionResult(
        val today: List<PulseStatistic>,
        val yesterday: List<PulseStatistic>,
        val currentYearList: List<PulseStatistic>,
        val previousYearsList: List<PulseStatistic>,
        val currentYear: Int,
    )
}