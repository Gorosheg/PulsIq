import android.content.Context
import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.statistics.R
import gorosheg.pulsiq.statistics.main.ui.mapper.UiPulseStatisticGroupBuilder
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatisticGroup
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter
import java.util.Locale

class UiPulseStatisticGroupBuilderTest {

    private val ruLocale: Locale = Locale.forLanguageTag("ru")
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM")
    private val todayDate: LocalDate = LocalDate.of(2024, 6, 15)

    @Test
    fun `GIVEN empty list WHEN buildUiGroups THEN returns empty groups`() {
        // given
        val builder = createBuilder()
        val items = emptyList<PulseStatistic>()

        // when
        val result = builder.buildUiGroups(items)

        // then
        assertTrue(result.isEmpty())
    }

    @Test
    fun `GIVEN today and yesterday items WHEN buildUiGroups THEN groups them correctly`() {
        // given
        val today = todayDate
        val yesterday = today.minusDays(1)
        val items = listOf(
            buildPulseStatisticModelTest(1, today.atTime(10, 0)),
            buildPulseStatisticModelTest(2, today.atTime(12, 30)),
            buildPulseStatisticModelTest(3, yesterday.atTime(8, 15))
        )

        // when
        val builder = createBuilder()
        val groups = builder.buildUiGroups(items)

        // then
        val todayItems = groups[0].items
        val yesterdayItems = groups[1].items
        val formatingToday = dateFormatter.format(today)
        val formatingYesterday = dateFormatter.format(yesterday)

        assertEquals(2, groups.size)
        assertEquals("Сегодня", groups[0].title)
        assertEquals("Вчера", groups[1].title)

        assertEquals(2, todayItems.size)
        assertEquals(2, todayItems[0].id)
        assertEquals(formatingToday, todayItems[0].dateStart)
        assertEquals(formatingToday, todayItems[1].dateStart)

        assertEquals(1, yesterdayItems.size)
        assertEquals(3, yesterdayItems[0].id)
        assertEquals(formatingYesterday, yesterdayItems[0].dateStart)
    }

    @Test
    fun `GIVEN current year items in different months WHEN buildUiGroups THEN builds month groups with correct titles`() {
        // given
        val currentYear = todayDate.year
        val janDate = LocalDate.of(currentYear, Month.JANUARY, 5).atTime(9, 0)
        val decDate = LocalDate.of(currentYear, Month.DECEMBER, 25).atTime(18, 45)
        val items = listOf(
            buildPulseStatisticModelTest(10, janDate),
            buildPulseStatisticModelTest(11, decDate)
        )

        // when
        val builder = createBuilder()
        val groups = builder.buildUiGroups(items)

        // then
        assertEquals(2, groups.size)
        assertEquals("Декабрь", groups[0].title)
        assertEquals("Январь", groups[1].title)

        val decItem = groups[0].items.first()
        val janItem = groups[1].items.first()
        assertEquals(dateFormatter.format(decDate.toLocalDate()), decItem.dateStart)
        assertEquals(dateFormatter.format(janDate.toLocalDate()), janItem.dateStart)
    }

    @Test
    fun `GIVEN items in same month WHEN buildUiGroups THEN month group items are sorted by date desc`() {
        // given
        val currentYear = todayDate.year
        val month = Month.MARCH
        val earlyDate = LocalDate.of(currentYear, month, 10).atTime(9, 0)
        val lateDate = LocalDate.of(currentYear, month, 15).atTime(20, 0)
        val items = listOf(
            buildPulseStatisticModelTest(1, earlyDate),
            buildPulseStatisticModelTest(2, lateDate)
        )

        // when
        val builder = createBuilder()
        val groups: List<UiPulseStatisticGroup> = builder.buildUiGroups(items)

        // then
        assertEquals(listOf(2, 1), groups.first().items.map { it.id })
    }

    @Test
    fun `GIVEN mixed items WHEN buildUiGroups THEN builds full hierarchy in correct order`() {
        // given
        val today = todayDate
        val yesterday = today.minusDays(1)
        val currentYear = today.year
        val previousYear = currentYear - 1

        val marchDate = LocalDate.of(currentYear, Month.MARCH, 10).atTime(9, 0)
        val aprilDate = LocalDate.of(currentYear, Month.APRIL, 5).atTime(18, 0)
        val previousYearDate = LocalDate.of(previousYear, Month.JUNE, 25).atTime(14, 0)

        val items = listOf(
            buildPulseStatisticModelTest(1, today.atTime(10, 0)),
            buildPulseStatisticModelTest(2, yesterday.atTime(17, 0)),
            buildPulseStatisticModelTest(3, marchDate),
            buildPulseStatisticModelTest(4, aprilDate),
            buildPulseStatisticModelTest(5, previousYearDate),
        )

        // when
        val builder = createBuilder()
        val groups = builder.buildUiGroups(items)

        // then
        assertEquals(
            listOf(
                "Сегодня",
                "Вчера",
                "Апрель",
                "Март",
                previousYear.toString(),
            ),
            groups.map { it.title }
        )
    }

    private fun mockContext(): Context {
        val context: Context = mock()
        whenever(context.getString(R.string.today)).thenReturn("Сегодня")
        whenever(context.getString(R.string.yesterday)).thenReturn("Вчера")
        return context
    }

    private fun createBuilder(): UiPulseStatisticGroupBuilder {
        return UiPulseStatisticGroupBuilder(
            context = mockContext(),
            ruLocale = ruLocale,
            todayProvider = { todayDate }
        )
    }

    private fun buildPulseStatisticModelTest(
        id: Int,
        date: LocalDateTime,
        name: String = "s$id"
    ): PulseStatistic = PulseStatistic(
        id = id,
        dateStart = date,
        dateEnd = null,
        name = name,
        pulse = emptyList()
    )
}