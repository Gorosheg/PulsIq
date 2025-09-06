package com.example.storage.settings

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    val lowerThresholdFlow: Flow<Int>

    val upperThresholdFlow: Flow<Int>

    fun saveThresholds(lower: Int, upper: Int)

    fun getLowerThreshold(): Int

    fun getUpperThreshold(): Int

    fun saveSoundVibration(soundEnabled: Boolean, vibrationEnabled: Boolean)

    fun getSoundEnabled(): Boolean

    fun getVibrationEnabled(): Boolean
}