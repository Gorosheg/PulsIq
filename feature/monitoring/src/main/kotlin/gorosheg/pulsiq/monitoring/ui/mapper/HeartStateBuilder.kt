package gorosheg.pulsiq.monitoring.ui.mapper

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.ColorUtils

internal object HeartStateBuilder {

    fun buildHeartColor(
        pulse: Int,
        minPulse: Int,
        maxPulse: Int
    ): Color {
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

    fun buildHeartRateSpeed(pulse: Int): Int {
        val clamped = pulse.coerceIn(40, 220)
        return 60_000 / clamped
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
}