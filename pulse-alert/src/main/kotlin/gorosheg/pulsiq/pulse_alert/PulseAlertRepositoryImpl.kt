package gorosheg.pulsiq.pulse_alert

import android.content.Context
import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import com.example.storage.ThresholdsRepository
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.utils.vibratorPermissionGranted
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PulseAlertRepositoryImpl(
    private val context: Context,
    private val heartBeatDataSource: HeartBeatDataSource,
    private val thresholdsRepository: ThresholdsRepository,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
    private val vibrator: Vibrator,
) : PulseAlertRepository {

    private var isInRecoveryState = false

    override fun initialize() {
        subscribeToPulse()
    }

    private fun subscribeToPulse() {
        combineTransform(
            heartBeatDataSource.subscribeHeartRateFlow(),
            thresholdsRepository.lowerThresholdFlow,
            thresholdsRepository.upperThresholdFlow
        ) { heartRate, lowerThreshold, upperThreshold ->
            when {
                heartRate >= upperThreshold -> emit(PulseType.HIGH)
                isInRecoveryState && heartRate <= lowerThreshold -> emit(PulseType.RECOVERY)
                else -> Unit
            }
        }
            .onEach { pulseType ->
                isInRecoveryState = pulseType == PulseType.HIGH
                pulseType.vibrate()
                pulseType.playBeep()
            }
            .launchIn(scope)
    }

    @Suppress("MissingPermission")
    private fun PulseType.vibrate() {
        if (!context.vibratorPermissionGranted) return
        val vibrator = vibrator

        val vibration = when (this) {
            PulseType.HIGH -> VibrationEffect.createOneShot(
                2000,
                VibrationEffect.DEFAULT_AMPLITUDE
            )

            PulseType.RECOVERY -> VibrationEffect.createWaveform(
                longArrayOf(0, 200, 200, 200),
                intArrayOf(0, 255, 0, 255),
                -1
            )
        }

        vibrator.vibrate(vibration)
    }

    private fun PulseType.playBeep() {
        val sound = when (this) {
            PulseType.HIGH -> ToneGenerator.TONE_PROP_BEEP2
            PulseType.RECOVERY -> ToneGenerator.TONE_PROP_BEEP
        }

        ToneGenerator(AudioManager.STREAM_ALARM, 100).startTone(sound, 500)
    }
}