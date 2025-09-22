package gorosheg.pulsiq.pulse_alert

import android.media.AudioManager
import android.media.ToneGenerator
import android.os.VibrationEffect
import android.os.Vibrator
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.storage.settings.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class PulseAlertRepositoryImpl(
    private val bluetoothRepository: BluetoothRepository,
    private val settingsRepository: SettingsRepository,
    private val vibrator: Vibrator?,
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.IO),
) : PulseAlertRepository {

    private var isInRecoveryState = false

    override fun initialize() {
        subscribeToPulse()
    }

    private fun subscribeToPulse() {
        combineTransform(
            bluetoothRepository.heartRateFlow,
            settingsRepository.lowerThresholdFlow,
            settingsRepository.upperThresholdFlow,
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
                pulseType.beep()
            }
            .launchIn(scope)
    }

    @Suppress("MissingPermission")
    private suspend fun PulseType.vibrate() {
        if (!settingsRepository.getVibrationEnabled()) return
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
        vibrator?.vibrate(vibration)
    }

    private suspend fun PulseType.beep() {
        if (!settingsRepository.getSoundEnabled()) return
        val sound = when (this) {
            PulseType.HIGH -> ToneGenerator.TONE_PROP_BEEP2
            PulseType.RECOVERY -> ToneGenerator.TONE_PROP_BEEP
        }

        ToneGenerator(AudioManager.STREAM_ALARM, 100).startTone(sound, 500)
    }
}