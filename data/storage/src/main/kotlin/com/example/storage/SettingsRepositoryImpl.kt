package com.example.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

internal class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) : SettingsRepository {

    override val lowerThresholdFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[KEY_LOWER] ?: 100
    }
    
    override val upperThresholdFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[KEY_UPPER] ?: 180
    }

    override fun saveThresholds(lower: Int, upper: Int) {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[KEY_LOWER] = lower
                preferences[KEY_UPPER] = upper
            }
        }
    }

    override fun getLowerThreshold(): Int = runBlocking {
        dataStore.data.map { preferences ->
            preferences[KEY_LOWER] ?: 100
        }.first()
    }
    
    override fun getUpperThreshold(): Int = runBlocking {
        dataStore.data.map { preferences ->
            preferences[KEY_UPPER] ?: 180
        }.first()
    }

    override fun saveSoundVibration(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        runBlocking {
            dataStore.edit { preferences ->
                preferences[KEY_SOUND_ENABLED] = soundEnabled
                preferences[KEY_VIBRATION_ENABLED] = vibrationEnabled
            }
        }
    }

    override fun getSoundEnabled(): Boolean = runBlocking {
        dataStore.data.map { preferences ->
            preferences[KEY_SOUND_ENABLED] ?: true
        }.first()
    }

    override fun getVibrationEnabled(): Boolean = runBlocking {
        dataStore.data.map { preferences ->
            preferences[KEY_VIBRATION_ENABLED] ?: true
        }.first()
    }

    companion object {
        private val KEY_LOWER = intPreferencesKey("lowerThreshold")
        private val KEY_UPPER = intPreferencesKey("upperThreshold")
        private val KEY_SOUND_ENABLED = booleanPreferencesKey("soundEnabled")
        private val KEY_VIBRATION_ENABLED = booleanPreferencesKey("vibrationEnabled")
    }
}