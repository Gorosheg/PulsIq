package gorosheg.pulsiq.storage.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal class SettingsRepositoryImpl(private val dataStore: DataStore<Preferences>) :
    SettingsRepository {

    override val lowerThresholdFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[KEY_LOWER] ?: 100
    }

    override val upperThresholdFlow: Flow<Int> = dataStore.data.map { preferences ->
        preferences[KEY_UPPER] ?: 180
    }

    override suspend fun saveThresholds(lower: Int, upper: Int) {
        dataStore.edit { preferences ->
            preferences[KEY_LOWER] = lower
            preferences[KEY_UPPER] = upper
        }
    }

    override suspend fun getLowerThreshold(): Int {
        return dataStore.data.map { preferences ->
            preferences[KEY_LOWER] ?: 100
        }.first()
    }

    override suspend fun getUpperThreshold(): Int {
        return dataStore.data.map { preferences ->
            preferences[KEY_UPPER] ?: 180
        }.first()
    }

    override suspend fun saveSoundVibration(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[KEY_SOUND_ENABLED] = soundEnabled
            preferences[KEY_VIBRATION_ENABLED] = vibrationEnabled
        }
    }

    override suspend fun getSoundEnabled(): Boolean {
        return dataStore.data.map { preferences ->
            preferences[KEY_SOUND_ENABLED] ?: true
        }.first()
    }

    override suspend fun getVibrationEnabled(): Boolean {
        return dataStore.data.map { preferences ->
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