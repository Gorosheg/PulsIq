package gorosheg.pulsiq.statistics.main.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.statistics.main.ui.component.MainStatisticCard
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatistic
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatisticGroup
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White

@Composable
internal fun StatisticsScreenContent(
    state: StatisticsUiState,
    onSwipe: (Int) -> Unit,
    onClick: (Int) -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        state.pulseStatisticList.forEachIndexed { index, group ->
            item(key = "header_${index}_${group.title}") {
                GroupHeader(title = group.title)
            }
            items(group.items, key = { it.id }) { item ->
                StatisticSwipeItem(
                    item = item,
                    onSwipe = onSwipe,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun GroupHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
        color = White,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
private fun StatisticSwipeItem(
    item: UiPulseStatistic,
    onSwipe: (Int) -> Unit,
    onClick: (Int) -> Unit,
) {
    val swipeState = rememberSwipeToDismissBoxState(confirmValueChange = { value ->
        if (value == SwipeToDismissBoxValue.EndToStart) {
            onSwipe(item.id)
            true
        } else {
            false
        }
    })

    SwipeToDismissBox(
        state = swipeState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {},
        content = { MainStatisticCard(item, onClick) }
    )
}

@Preview
@Composable
private fun StatisticsScreenContentPreview() {
    MyAppTheme {
        StatisticsScreenContent(
            state = StatisticsUiState(
                pulseStatisticList = listOf(
                    UiPulseStatisticGroup(
                        title = "Сегодня",
                        items = listOf(
                            UiPulseStatistic(
                                id = 1,
                                name = "Morning Run",
                                dateStart = "26.10. 10:00",
                            ),
                            UiPulseStatistic(
                                id = 2,
                                name = "Evening Walk",
                                dateStart = "26.10. 18:00",
                            )
                        )
                    ),
                    UiPulseStatisticGroup(
                        title = "Октябрь",
                        items = listOf(
                            UiPulseStatistic(
                                id = 3,
                                name = "Gym",
                                dateStart = "15.10. 19:30",
                            )
                        )
                    )
                )
            ),
            onSwipe = {},
            onClick = {}
        )
    }
}