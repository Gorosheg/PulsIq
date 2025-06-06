package gorosheg.pulsiq.monitoring.ui

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MonitoringScreenContent(
    multiplePermissionState: MultiplePermissionsState,
    state: MonitoringUiState,
    startTracking: () -> Unit,
    stopTracking: () -> Unit
) {
    val pulseColor = if (state.pulse >= 100) Color(0xFFFF5E5E) else Color(0xFF3DDC84)
    val heartScale by rememberInfiniteTransition(label = "Heart Beat").animateFloat(
        initialValue = 1f,
        targetValue = 1.3f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Heart Scale"
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF6F6FA)
    ) {
        if (multiplePermissionState.allPermissionsGranted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .size(96.dp)
                            .graphicsLayer {
                                scaleX = heartScale
                                scaleY = heartScale
                            },
                        tint = pulseColor
                    )
                    Text(
                        text = "${state.pulse}",
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(Modifier.height(32.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    if (!state.isTracking) {
                        Button(
                            onClick = startTracking,
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3DDC84))
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Старт", fontWeight = FontWeight.Medium)
                        }
                    } else {
                        OutlinedButton(
                            onClick = stopTracking,
                            border = BorderStroke(2.dp, Color(0xFFDD2C00)),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFDD2C00))
                        ) {
                            Icon(Icons.Default.Close, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Остановить", fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Разрешения Bluetooth и геолокации не предоставлены",
                    fontSize = 18.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
