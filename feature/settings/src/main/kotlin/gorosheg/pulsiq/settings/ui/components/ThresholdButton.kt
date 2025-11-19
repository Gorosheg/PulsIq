package gorosheg.pulsiq.settings.ui.components

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White
import kotlinx.coroutines.delay

@Composable
fun ThresholdButton(
    icon: ImageVector,
    contentDescription: String,
    onStep: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    var hadRepeats by remember { mutableStateOf(false) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            hadRepeats = false

            val initialDelayMs = 400L
            delay(initialDelayMs)

            var intervalMs = 160L
            val minIntervalMs = 40L
            val accel = 0.85f

            while (isPressed) {
                onStep()
                hadRepeats = true
                delay(intervalMs)
                intervalMs = maxOf(minIntervalMs, (intervalMs * accel).toLong())
            }
        }
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .clip(RoundedCornerShape(12.dp))
            .pointerInput(Unit) {
                awaitEachGesture {
                    awaitFirstDown(requireUnconsumed = false)
                    isPressed = true
                    val up = waitForUpOrCancellation()
                    isPressed = false

                    if (up != null && !hadRepeats) {
                        onStep()
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = White
        )
    }
}

@Preview()
@Composable
private fun ThresholdButtonPreview() {
    MyAppTheme {
        ThresholdButton(
            icon = Icons.Filled.KeyboardArrowDown,
            contentDescription = stringResource(R.string.decrease_button_description),
            onStep = { }
        )
    }
}
