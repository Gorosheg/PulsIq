package gorosheg.pulsiq.monitoring.presentation

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class PulseAlertController(private val context: Context) {

    private var inHighAlertState = false

    fun onPulseChanged(bpm: Int) {
        if (!inHighAlertState && bpm >= 100) {
            inHighAlertState = true
            triggerAlert(AlertType.HIGH)
        } else if (inHighAlertState && bpm <= 80) {
            inHighAlertState = false
            triggerAlert(AlertType.NORMAL)
        }
    }

    private fun triggerAlert(type: AlertType) {
        when (type) {
            AlertType.HIGH -> {
                vibrateStrong()
                playHighBeep()
            }

            AlertType.NORMAL -> {
                vibrateSoft()
                playLowBeep()
            }
        }
    }

    @Suppress("MissingPermission")
    private fun vibrateStrong() {
        val vibrator = getVibrator() ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    2000,
                    VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(2000)
        }
    }

    @Suppress("MissingPermission")
    private fun vibrateSoft() {
        val vibrator = getVibrator() ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(0, 200, 200, 200),
                    intArrayOf(0, 255, 0, 255),
                    -1
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 200, 200, 200), -1)
        }
    }

    private fun playHighBeep() {
        ToneGenerator(AudioManager.STREAM_ALARM, 100).startTone(
            ToneGenerator.TONE_PROP_BEEP2,
            500
        )
    }

    private fun playLowBeep() {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100).startTone(
            ToneGenerator.TONE_PROP_BEEP,
            300
        )
    }

    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}

private enum class AlertType {
    HIGH, NORMAL
}

