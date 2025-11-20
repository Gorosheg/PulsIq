package gorosheg.pulsiq.statistics.di

import gorosheg.pulsiq.statistics.StatisticsRepository
import gorosheg.pulsiq.statistics.StatisticsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val statisticsRepositoryModule = module {
    singleOf(::StatisticsRepositoryImpl) bind StatisticsRepository::class
}