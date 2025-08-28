package gorosheg.pulsiq.statistics.di

import gorosheg.pulsiq.statistics.navigation.StatisticsScreenProvider
import gorosheg.pulsiq.statistics.presentation.StatisticsViewModel
import gorosheg.pulsiq.statistics.ui.StatisticsScreen
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val statisticsModule = module {
    factory { StatisticsScreenProvider(::StatisticsScreen) }

    viewModelOf(::StatisticsViewModel)
}