package gorosheg.pulsiq.statistics.di

import gorosheg.pulsiq.statistics.navigation.StatisticsProvider
import gorosheg.pulsiq.statistics.presentation.StatisticsViewModel
import gorosheg.pulsiq.statistics.ui.StatisticsScreen
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val statisticsModule = module {
    factory { StatisticsProvider(::StatisticsScreen) }
    viewModel { StatisticsViewModel() }
}