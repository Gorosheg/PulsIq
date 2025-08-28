package gorosheg.pulsiq.ui.alert // todo separate module

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.content.ContextCompat
import com.example.storage.ThresholdsRepository
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class PulseAlertRepositoryImpl(
    private val context: Context,
    private val heartBeatDataSource: HeartBeatDataSource,
    private val thresholdsRepository: ThresholdsRepository,
) : PulseAlertRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)//todo constructor
    private var inHighAlertState = false // todo rename
    private var vibratorPermissionGranted: Boolean = false

    @Volatile
    private var lowerThreshold: Int = thresholdsRepository.getLowerThreshold() // todo remove

    @Volatile
    private var upperThreshold: Int = thresholdsRepository.getUpperThreshold()

    override fun initialize() {
        vibratorPermissionGranted = ContextCompat.checkSelfPermission( // todo getter
            context, // todo separate val extencion context
            Manifest.permission.VIBRATE
        ) == PackageManager.PERMISSION_GRANTED

        scope.launch {
            thresholdsRepository.lowerThresholdFlow.collectLatest { lower ->
                lowerThreshold = lower
            }
            thresholdsRepository.upperThresholdFlow.collectLatest { upper ->
                upperThreshold = upper
            }
        }

        subscribeToPulse()

    }

    private fun subscribeToPulse() {
        scope.launch {
            heartBeatDataSource.heartRateFlow.collectLatest(::onPulseChanged)  // todo use combine with thresholds
        }
    }

    private fun onPulseChanged(bpm: Int) {
        when {
            bpm >= upperThreshold -> {
                inHighAlertState = true
                triggerAlert(AlertType.HIGH)
            }

            inHighAlertState && bpm <= lowerThreshold -> {
                inHighAlertState = false
                triggerAlert(AlertType.NORMAL)
            }
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
    private fun vibrateStrong() { // todo type: AlertType
        if (!vibratorPermissionGranted) return
        val vibrator = getVibrator() ?: return

        vibrator.vibrate( // todo create extension with type: AlertType
            VibrationEffect.createOneShot(
                2000,
                VibrationEffect.DEFAULT_AMPLITUDE
            )
        )
    }

    @Suppress("MissingPermission")
    private fun vibrateSoft() {
        if (!vibratorPermissionGranted) return
        val vibrator = getVibrator() ?: return

        vibrator.vibrate(
            VibrationEffect.createWaveform(
                longArrayOf(0, 200, 200, 200),
                intArrayOf(0, 255, 0, 255),
                -1
            )
        )
    }

    private fun playHighBeep() {
        ToneGenerator(AudioManager.STREAM_ALARM, 100).startTone(// todo create extension with type: AlertType
            ToneGenerator.TONE_PROP_BEEP2,
            500
        )
    }

    private fun playLowBeep() {
        ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100).startTone(
            ToneGenerator.TONE_PROP_BEEP,
            500
        )
    }

    // todo construcorot
    private fun getVibrator(): Vibrator? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}

// todo separate file
private enum class AlertType {
    HIGH,
    NORMAL
}

