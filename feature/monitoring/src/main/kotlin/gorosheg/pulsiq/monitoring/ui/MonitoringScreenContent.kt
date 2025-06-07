package gorosheg.pulsiq.monitoring.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
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
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.Crimson
import gorosheg.pulsiq.ui.Orange
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MonitoringScreenContent(
    multiplePermissionState: MultiplePermissionsState,
    state: MonitoringUiState,
    startTracking: () -> Unit,
    stopTracking: () -> Unit
) {
    val targetColor = when {
        state.pulse < 87 -> Blue
        state.pulse < 93 -> Orange
        else -> Crimson
    }

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = tween(durationMillis = 500),
        label = "PulseColor"
    )

    val scale = remember { Animatable(1f) }
    val pulse = rememberUpdatedState(newValue = state.pulse)

    LaunchedEffect(pulse.value) {
        scale.stop()

        val speed = when (pulse.value) {
            in 0..87 -> 2000
            in 88..94 -> 1000
            else -> 500
        }

        launch {
            while (isActive) {
                scale.animateTo(1.3f, tween(speed, easing = FastOutSlowInEasing))
                scale.animateTo(1f, tween(speed, easing = FastOutSlowInEasing))
            }
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (multiplePermissionState.allPermissionsGranted) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 32.dp)
                    .padding(top = 48.dp, bottom = 32.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(horizontal = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(bottom = 32.dp),
                        ) {
                            Icon(
                                Icons.Filled.Favorite,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(280.dp)
                                    .graphicsLayer {
                                        scaleX = scale.value
                                        scaleY = scale.value
                                    },
                                tint = animatedColor
                            )

                            Text(
                                text = state.pulse.toString(),
                                fontSize = 72.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp)
                            .align(Alignment.BottomCenter),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (!state.isTracking) {
                            Button(
                                onClick = startTracking,
                                modifier = Modifier
                                    .height(64.dp)
                                    .width(220.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Blue)
                            ) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Text("Старт", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                            }
                        } else {
                            OutlinedButton(
                                onClick = stopTracking,
                                modifier = Modifier
                                    .height(64.dp)
                                    .width(220.dp),
                                border = BorderStroke(3.dp, Crimson),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Crimson)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = null)
                                Spacer(Modifier.width(12.dp))
                                Text("Остановить", fontSize = 20.sp, fontWeight = FontWeight.Medium)
                            }
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
                    fontSize = 20.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
