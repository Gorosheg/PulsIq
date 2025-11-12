package gorosheg.pulsiq.statistics.tracking_session.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gorosheg.pulsiq.statistics.tracking_session.ui.model.ChartState
import gorosheg.pulsiq.ui.BlueGray
import gorosheg.pulsiq.ui.White


@Composable
internal fun PulseDynamics(chartState: ChartState?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = BlueGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Динамика пульса",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = White
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Пульс",
                        style = MaterialTheme.typography.bodySmall.copy(
                            platformStyle = PlatformTextStyle(includeFontPadding = false),
                            lineHeight = MaterialTheme.typography.bodySmall.fontSize,
                            letterSpacing = 0.sp
                        ),
                        color = White,
                        maxLines = 1,
                        softWrap = false,
                        overflow = TextOverflow.Visible,
                        modifier = Modifier
                            .graphicsLayer { rotationZ = -90f }
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    if (chartState == null) {
                        EmptyPulseChart()
                    } else {
                        PulseChart(chartState)
                    }
                }
            }

            Text(
                text = "Время",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "PulseDynamics Preview")
@Composable
private fun PulseDynamicsPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        PulseDynamics(
            chartState = ChartState(
                timeList = listOf(0, 1, 2, 3, 4, 5, 6, 7),
                pulseList = listOf(60, 72, 80, 95, 110, 100, 88, 76),
                chartPadding = 3.0,
            )
        )
    }
}
