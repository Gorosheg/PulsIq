package gorosheg.pulsiq.storage.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val lowerThresholdFlow: Flow<Int>

    val upperThresholdFlow: Flow<Int>

    suspend fun saveThresholds(lower: Int, upper: Int)

    suspend fun getLowerThreshold(): Int

    suspend fun getUpperThreshold(): Int

    suspend fun saveSoundVibration(soundEnabled: Boolean, vibrationEnabled: Boolean)

    suspend fun getSoundEnabled(): Boolean

    suspend fun getVibrationEnabled(): Boolean
}