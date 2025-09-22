package gorosheg.pulsiq.storage

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import gorosheg.pulsiq.storage.statistics.StatisticsDatabaseDatasource
import gorosheg.pulsiq.storage.settings.SettingsRepository
import gorosheg.pulsiq.storage.settings.SettingsRepositoryImpl
import gorosheg.pulsiq.storage.statistics.StatisticsDatabase
import gorosheg.pulsiq.storage.statistics.StatisticsDatabaseDatasourceImpl
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