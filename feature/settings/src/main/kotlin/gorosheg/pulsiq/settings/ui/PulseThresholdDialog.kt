package gorosheg.pulsiq.settings.ui

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.White
import kotlinx.coroutines.delay

@Composable
internal fun PulseThresholdDialog(
    lowerThreshold: Int,
    upperThreshold: Int,
    onDismiss: () -> Unit,
    onApply: (Int, Int) -> Unit
) {
    var currentLowerThreshold by remember { mutableIntStateOf(lowerThreshold) }
    var currentUpperThreshold by remember { mutableIntStateOf(upperThreshold) }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {},
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ThresholdSelector(
                    title = stringResource(R.string.min_threshold_title),
                    value = currentLowerThreshold,
                    onValueChange = { new ->
                        currentLowerThreshold = new.coerceIn(0, minOf(currentUpperThreshold, 250))
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ThresholdSelector(
                    title = stringResource(R.string.max_threshold_title),
                    value = currentUpperThreshold,
                    onValueChange = { new ->
                        currentUpperThreshold = new.coerceIn(maxOf(currentLowerThreshold, 0), 250)
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                ApplyButton(
                    onClick = {
                        onApply(currentLowerThreshold, currentUpperThreshold)
                    }
                )
            }
        },
        shape = RoundedCornerShape(16.dp),
    )
}

@Composable
private fun ThresholdSelector(
    title: String,
    value: Int,
    onValueChange: (Int) -> Unit
) {
    val currentValue by rememberUpdatedState(value)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            ThresholdButton(
                icon = Icons.Filled.KeyboardArrowDown,
                contentDescription = stringResource(R.string.decrease_button_description),
                onStep = {
                    if (currentValue > 0) onValueChange(currentValue - 1)
                }
            )

            Text(
                text = "$currentValue",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

            ThresholdButton(
                icon = Icons.Filled.KeyboardArrowUp,
                contentDescription = stringResource(R.string.increase_button_description),
                onStep = {
                    onValueChange((currentValue + 1).coerceAtMost(250))
                }
            )
        }
    }
}

@Composable
private fun ThresholdButton(
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

@Composable
private fun ApplyButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Blue),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(
            text = stringResource(R.string.apply_button_text),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}