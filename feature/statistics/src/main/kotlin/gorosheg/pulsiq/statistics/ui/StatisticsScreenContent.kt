package gorosheg.pulsiq.statistics.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gorosheg.pulsiq.ui.Black

@Composable
internal fun StatisticsScreenContent(
) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        text = "Статистика",
        color = Black,
        fontSize = 20.sp,
    )
}