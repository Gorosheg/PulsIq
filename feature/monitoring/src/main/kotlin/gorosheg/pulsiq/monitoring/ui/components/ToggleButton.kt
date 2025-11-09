package gorosheg.pulsiq.monitoring.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gorosheg.pulsiq.monitoring.R
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.Crimson


@Composable
internal fun ToggleButton(
    isTracking: Boolean,
    startTracking: () -> Unit,
    stopTracking: () -> Unit
) {
    val onClick = if (isTracking) stopTracking else startTracking

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        if (!isTracking) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .height(64.dp)
                    .width(220.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue)
            ) {
                ToggleButtonContent(icon = Icons.Default.PlayArrow, text = R.string.start_button_text)
            }
        } else {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier
                    .height(64.dp)
                    .width(220.dp),
                border = BorderStroke(3.dp, Crimson),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Crimson)
            ) {
                ToggleButtonContent(icon = Icons.Default.Close, R.string.stop_button_text)
            }
        }
    }
}

@Composable
private fun ToggleButtonContent(
    icon: ImageVector,
    text: Int
) {
    Icon(icon, contentDescription = null)
    Spacer(Modifier.width(12.dp))
    Text(
        stringResource(text),
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )
}