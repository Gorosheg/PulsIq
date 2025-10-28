package gorosheg.pulsiq.statistics.tracking_session.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoScrollState
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionUiState

@Composable
internal fun TrackingSessionScreenContent(state: TrackingSessionUiState) {
    val modelProducer = remember { CartesianChartModelProducer() }

    if (state.pulse.isNotEmpty()) {
        LaunchedEffect(state.pulse) {
            modelProducer.runTransaction {
                lineSeries {
                    series(state.pulse.map { it.toFloat() as Number })
                }
            }
        }
    }

    val chart = rememberCartesianChart(
        rememberLineCartesianLayer(),
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        CartesianChartHost(
            chart = chart,
            modelProducer = modelProducer,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp),
            scrollState = rememberVicoScrollState(scrollEnabled = false),
        )

        Text(
            text = state.name,
            style = MaterialTheme.typography.titleMedium,
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = state.dateStart, style = MaterialTheme.typography.bodyMedium)
            Text(text = "—", style = MaterialTheme.typography.bodyMedium)
            Text(text = state.dateEnd, style = MaterialTheme.typography.bodyMedium)
        }

        Text(text = "Макс: ${state.highestPulse}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Мин: ${state.lowestPulse}", style = MaterialTheme.typography.bodyMedium)
        Text(text = "Средний: ${state.averagePulse}", style = MaterialTheme.typography.bodyMedium)
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "TrackingSessionScreenContent Preview")
@Composable
private fun TrackingSessionScreenContentPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        val pulse = listOf(72, 78, 85, 92, 110, 105, 98, 88, 95, 102, 96, 90)
        TrackingSessionScreenContent(
            state = TrackingSessionUiState(
                name = "Утренняя тренировка",
                dateStart = "26.10. 10:00",
                dateEnd = "26.10. 11:00",
                pulse = pulse,
                highestPulse = pulse.maxOrNull() ?: 0,
                lowestPulse = pulse.minOrNull() ?: 0,
                averagePulse = if (pulse.isNotEmpty()) pulse.average().toInt() else 0,
            )
        )
    }
}
