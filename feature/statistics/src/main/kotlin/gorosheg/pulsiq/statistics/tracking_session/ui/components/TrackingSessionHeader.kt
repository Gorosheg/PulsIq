package gorosheg.pulsiq.statistics.tracking_session.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gorosheg.pulsiq.statistics.tracking_session.ui.model.TrackingSessionHeaderState
import gorosheg.pulsiq.ui.BlueGray
import gorosheg.pulsiq.ui.White

@Composable
internal fun TrackingSessionHeader(
    state: TrackingSessionHeaderState,
    onEditClick: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = BlueGray),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val date = state.dateStart.substringBeforeLast(". ").ifBlank { state.dateStart }
            val timeStart = state.dateStart.substringAfterLast(" ")
            val timeEnd = state.dateEnd.substringAfterLast(" ")

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = state.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = White,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    CapsuleIcon(icon = Icons.Default.Create, onClick = onEditClick)
                }

                Text(
                    text = date,
                    style = MaterialTheme.typography.bodyMedium,
                    color = White,
                )
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Capsule(text = timeStart)
                Text(
                    text = "—",
                    style = MaterialTheme.typography.bodyMedium,
                    color = White,
                    modifier = Modifier.offset(y = 1.dp)
                )
                Capsule(text = timeEnd)
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true, name = "TrackingSessionHeader Preview")
@Composable
private fun TrackingSessionHeaderPreview() {
    gorosheg.pulsiq.ui.MyAppTheme {
        TrackingSessionHeader(
            state = TrackingSessionHeaderState(
                name = "Утренняя тренировка",
                dateStart = "26.10. 10:00",
                dateEnd = "26.10. 11:00",
            ),
            onEditClick = {}
        )
    }
}
