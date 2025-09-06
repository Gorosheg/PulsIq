package gorosheg.pulsiq.statistics_repository.di

import gorosheg.pulsiq.statistics_repository.StatisticsRepository
import gorosheg.pulsiq.statistics_repository.StatisticsRepositoryImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val statisticsRepositoryModule = module {

    singleOf(::StatisticsRepositoryImpl) bind StatisticsRepository::class
}