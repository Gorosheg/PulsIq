package com.example.storage

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

internal class ThresholdsRepositoryImpl(private val dataStore: DataStore<Preferences>) : ThresholdsRepository {

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

    companion object {
        private val KEY_LOWER = intPreferencesKey("lowerThreshold")
        private val KEY_UPPER = intPreferencesKey("upperThreshold")
    }
}
