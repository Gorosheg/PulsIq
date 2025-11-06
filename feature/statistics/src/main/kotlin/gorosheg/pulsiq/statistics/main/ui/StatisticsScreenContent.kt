package gorosheg.pulsiq.statistics.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gorosheg.pulsiq.statistics.main.ui.model.StatisticsUiState
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatistic
import gorosheg.pulsiq.statistics.main.ui.model.UiPulseStatisticGroup
import gorosheg.pulsiq.ui.BlueGray
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

@OptIn(ExperimentalMaterial3Api::class)
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
        content = { StatisticCard(item, onClick) }
    )
}

@Composable
private fun StatisticCard(
    item: UiPulseStatistic,
    onClick: (Int) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
            ) { onClick(item.id) },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = BlueGray),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = White
            )
            Text(text = item.dateStart, fontSize = 12.sp, color = White)
        }
    }
}

@Preview(showBackground = true, name = "StatisticsScreenContent Preview")
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