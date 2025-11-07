package gorosheg.pulsiq.monitoring.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import gorosheg.pulsiq.monitoring.R
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.Crimson
import gorosheg.pulsiq.ui.EditNameDialog

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MonitoringScreenContent(
    state: MonitoringUiState,
    startTracking: () -> Unit,
    stopTracking: () -> Unit,
    onSessionNameChanged: (String) -> Unit,
    onNameDialogDismiss: () -> Unit,
) {
    val animatedColor by animateColorAsState(
        targetValue = state.heartColor,
        animationSpec = tween(durationMillis = 1000),
    )

    var scaleAnimation by remember { mutableFloatStateOf(1f) }

    HeartRateAnimations(
        pulse = state.pulse,
        heartRateSpeed = state.heartRateSpeed,
        onScaleChange = { scaleAnimation = it }
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        PermissionGrantedContent(
            state = state,
            startTracking = startTracking,
            stopTracking = stopTracking,
            animatedColor = animatedColor,
            scaleAnimation = scaleAnimation
        )
    }

    if (state.isSetNameDialogShow) {
        EditNameDialog(
            name = state.sessionName,
            onNameChanged = { onSessionNameChanged(it) },
            onCloseEditDialogClick = { onNameDialogDismiss() }
        )
    }
}

@Composable
private fun PermissionGrantedContent(
    state: MonitoringUiState,
    startTracking: () -> Unit,
    stopTracking: () -> Unit,
    animatedColor: Color,
    scaleAnimation: Float
) {
    Scaffold(
        bottomBar = {
            ToggleButton(
                isTracking = state.isTracking,
                startTracking = startTracking,
                stopTracking = stopTracking
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            HeartDisplay(
                pulse = state.pulse,
                heartColor = animatedColor,
                scaleAnimation = scaleAnimation,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}


@Composable
private fun HeartRateAnimations(
    pulse: Int,
    heartRateSpeed: Int,
    onScaleChange: (Float) -> Unit
) {
    val scaleAnimation = remember { Animatable(1f) }
    val currentSpeed by rememberUpdatedState(heartRateSpeed)

    LaunchedEffect(pulse > 0) {
        if (pulse <= 0) {
            scaleAnimation.animateTo(
                1f,
                animationSpec = tween(300)
            )
        } else {
            while (true) {
                val half = (currentSpeed / 2).coerceAtLeast(50)
                scaleAnimation.animateTo(1.3f, tween(durationMillis = half, easing = EaseInOut))
                scaleAnimation.animateTo(1f, tween(durationMillis = half, easing = EaseInOut))
            }
        }
    }

    LaunchedEffect(scaleAnimation.value) {
        onScaleChange(scaleAnimation.value)
    }
}

@Composable
private fun HeartDisplay(
    pulse: Int,
    heartColor: Color,
    scaleAnimation: Float,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Icon(
            imageVector = Icons.Filled.Favorite,
            contentDescription = null,
            modifier = Modifier
                .size(280.dp)
                .scale(scaleAnimation),
            tint = heartColor
        )
        Text(
            text = pulse.toString(),
            fontSize = 72.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

@Composable
private fun ToggleButton(
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
fun ToggleButtonContent(icon: ImageVector, text: Int) {
    Icon(icon, contentDescription = null)
    Spacer(Modifier.width(12.dp))
    Text(
        stringResource(text),
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true)
@Composable
private fun MonitoringScreenContentPreview() {
    MonitoringScreenContent(
        state = MonitoringUiState(
            isTracking = false,
            pulse = 0,
            heartColor = Blue,
            heartRateSpeed = 100,
        ),
        startTracking = {},
        stopTracking = {},
        onSessionNameChanged = {},
        onNameDialogDismiss = {},
    )
}

@OptIn(ExperimentalPermissionsApi::class)
@Preview(showBackground = true, name = "Tracking State")
@Composable
private fun MonitoringScreenContentTrackingPreview() {
    MonitoringScreenContent(
        state = MonitoringUiState(
            isTracking = true,
            pulse = 120,
            heartColor = Crimson,
            heartRateSpeed = 100,
        ),
        startTracking = {},
        stopTracking = {},
        onSessionNameChanged = {},
        onNameDialogDismiss = {},
    )
}