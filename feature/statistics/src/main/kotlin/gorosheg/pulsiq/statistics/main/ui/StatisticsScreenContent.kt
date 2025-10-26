package gorosheg.pulsiq.statistics.main.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import gorosheg.pulsiq.ui.BlueGray
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White

@Composable
internal fun StatisticsScreenContent(
    state: StatisticsUiState
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.pulseStatisticList, key = { it.id }) { item ->
            StatisticCard(item)
        }
    }
}

@Composable
private fun StatisticCard(item: UiPulseStatistic) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(),
            ) { },
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
                    UiPulseStatistic(
                        id = 1,
                        dateStart = "26.10. 10:00",
                        dateEnd = "26.10. 10:30",
                        name = "Morning Run",
                        pulse = listOf(80, 95, 100),
                        highestPulse = 120,
                        lowestPulse = 70,
                        averagePulse = 90
                    ),
                    UiPulseStatistic(
                        id = 2,
                        dateStart = "26.10. 18:00",
                        dateEnd = "26.10. 18:45",
                        name = "Evening Walk",
                        pulse = listOf(75, 85, 88),
                        highestPulse = 100,
                        lowestPulse = 65,
                        averagePulse = 80
                    )
                )
            )
        )
    }
}