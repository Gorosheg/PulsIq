package com.example.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val storageModule = module {
    single<DataStore<Preferences>> { 
        get<Context>().dataStore
    }

    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
}