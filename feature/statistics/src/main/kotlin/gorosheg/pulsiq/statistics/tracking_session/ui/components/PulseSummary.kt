package gorosheg.pulsiq.statistics.tracking_session.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.statistics.tracking_session.ui.model.PulseSummaryState
import gorosheg.pulsiq.ui.BlueGray
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White

@Composable
internal fun PulseSummary(state: PulseSummaryState) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 2.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = BlueGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Пульс, сводка",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = White
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                PulseStatisticCard(
                    label = "Макс",
                    value = "${state.highestPulse} уд/мин",
                    modifier = Modifier.weight(1f)
                )
                PulseStatisticCard(
                    label = "Мин",
                    value = "${state.lowestPulse} уд/мин",
                    modifier = Modifier.weight(1f)
                )
                PulseStatisticCard(
                    label = "Средний",
                    value = "${state.averagePulse} уд/мин",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "PulseSummary Preview")
@Composable
private fun PulseSummaryPreview() {
    MyAppTheme {
        PulseSummary(
            state = PulseSummaryState(
                highestPulse = 178,
                lowestPulse = 62,
                averagePulse = 112,
            )
        )
    }
}