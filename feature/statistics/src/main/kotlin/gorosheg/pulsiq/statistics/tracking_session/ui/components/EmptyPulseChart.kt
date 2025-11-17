package gorosheg.pulsiq.statistics.tracking_session.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.statistics.R
import gorosheg.pulsiq.ui.Gray
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White

@Composable
internal fun BoxScope.EmptyPulseChart() {
    Surface(
        modifier = Modifier
            .matchParentSize()
            .clip(MaterialTheme.shapes.large),
        color = Gray
    ) {}
    Text(
        text = stringResource(R.string.empty_pulse_chart),
        style = MaterialTheme.typography.bodyMedium,
        color = White,
        modifier = Modifier.align(Alignment.Center)
    )
}

@Preview()
@Composable
private fun EmptyPulseChartPreview() {
    MyAppTheme {
        Box(modifier = Modifier.size(320.dp, 180.dp)) {
            EmptyPulseChart()
        }
    }
}