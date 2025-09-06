package com.example.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.storage.settings.SettingsRepository
import com.example.storage.settings.SettingsRepositoryImpl
import com.example.storage.statistics.StatisticsDatabase
import com.example.storage.statistics.StatisticsDatabaseDatasource
import com.example.storage.statistics.StatisticsDatabaseDatasourceImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val storageModule = module {
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }

    single<DataStore<Preferences>> {
        get<Context>().dataStore
    }

    single<StatisticsDatabaseDatasource> {
        StatisticsDatabaseDatasourceImpl(
            statisticsDao = get(),
        )
    }

    single { get<StatisticsDatabase>().statisticsDao }

    single {
        Room.databaseBuilder(
            androidContext(),
            StatisticsDatabase::class.java,
            "database"
        ).build()
    }
}