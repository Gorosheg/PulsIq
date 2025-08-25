package gorosheg.pulsiq.monitoring.ui

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils
import gorosheg.pulsiq.common.viewModel.uiStateMapper.UiStateMapper
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState

internal class MonitoringUiStateMapper : UiStateMapper<MonitoringState, MonitoringUiState> {
    override fun MonitoringState.map(): MonitoringUiState {
        return MonitoringUiState(
            isTracking = isTracking,
            pulse = pulse,
            heartColor = defineHeartColor(
                pulse,
                lowerThreshold,
                upperThreshold
            ),
            heartRateSpeed = defineHeartRateSpeed(pulse),
        )
    }

    private fun defineHeartColor(
        pulse: Int,
        lowerThreshold: Int,
        upperThreshold: Int
    ): Color {
        val minPulse = lowerThreshold
        val maxPulse = upperThreshold
        val clamped = pulse.coerceIn(minPulse, maxPulse)
        val fraction = (clamped - minPulse).toFloat() / (maxPulse - minPulse)

        val startArgb = 0xFF4CCAE3.toInt()
        val endArgb = 0xFFFF4D6D.toInt()

        val startHSL = FloatArray(3)
        val endHSL = FloatArray(3)

        ColorUtils.colorToHSL(startArgb, startHSL)
        ColorUtils.colorToHSL(endArgb, endHSL)

        val interpolatedHSL = FloatArray(3) { i ->
            interpolateColorHSL(startHSL[i], endHSL[i], fraction, i == 0)
        }

        val interpolatedArgb = ColorUtils.HSLToColor(interpolatedHSL)
        return Color(interpolatedArgb)
    }

    private fun interpolateColorHSL(
        start: Float,
        end: Float,
        fraction: Float,
        isHue: Boolean
    ): Float {
        return if (isHue) {
            val diff = ((end - start + 360) % 360).let { if (it > 180) it - 360 else it }
            (start + diff * fraction + 360) % 360
        } else {
            start + (end - start) * fraction
        }
    }

    private fun defineHeartRateSpeed(pulse: Int): Int {
        val clamped = pulse.coerceIn(40, 220)
        return 60_000 / clamped
    }
}