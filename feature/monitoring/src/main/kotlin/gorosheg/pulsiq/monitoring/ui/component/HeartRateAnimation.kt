package gorosheg.pulsiq.monitoring.ui.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState

@Composable
internal fun HeartRateAnimation(
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
