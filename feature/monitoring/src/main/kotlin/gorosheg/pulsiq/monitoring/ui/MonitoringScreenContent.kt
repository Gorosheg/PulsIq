package gorosheg.pulsiq.monitoring.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import gorosheg.pulsiq.monitoring.ui.components.HeartImage
import gorosheg.pulsiq.monitoring.ui.components.HeartRateAnimation
import gorosheg.pulsiq.monitoring.ui.components.ToggleButton
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.Crimson
import gorosheg.pulsiq.ui.EditNameDialog
import gorosheg.pulsiq.ui.MyAppTheme

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

    HeartRateAnimation(
        pulse = state.pulse,
        heartRateSpeed = state.heartRateSpeed,
        onScaleChange = { scaleAnimation = it }
    )

    Surface(modifier = Modifier.fillMaxSize()) {
        MainContent(
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
private fun MainContent(
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
            HeartImage(
                pulse = state.pulse,
                heartColor = animatedColor,
                scaleAnimation = scaleAnimation,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Preview()
@Composable
private fun MonitoringScreenContentPreview() {
    MyAppTheme {
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
}

@Preview()
@Composable
private fun MonitoringScreenContentTrackingPreview() {
    MyAppTheme {
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
}