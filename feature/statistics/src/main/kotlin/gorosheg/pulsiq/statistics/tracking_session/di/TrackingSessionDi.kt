package gorosheg.pulsiq.statistics.tracking_session.di

import gorosheg.pulsiq.statistics.main.navigation.StatisticsScreenProvider
import gorosheg.pulsiq.statistics.main.presentation.StatisticsViewModel
import gorosheg.pulsiq.statistics.main.ui.StatisticsScreen
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val trackingSessionModule = module {
    factory { StatisticsScreenProvider(::StatisticsScreen) }

    viewModelOf(::StatisticsViewModel)
}