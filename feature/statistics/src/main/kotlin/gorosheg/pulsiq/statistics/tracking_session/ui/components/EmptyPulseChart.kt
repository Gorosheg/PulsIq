package gorosheg.pulsiq.statistics.tracking_session.ui.components

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import gorosheg.pulsiq.ui.Gray
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
        text = "Нет данных для построения графика",
        style = MaterialTheme.typography.bodyMedium,
        color = White,
        modifier = Modifier.align(Alignment.Center)
    )
}