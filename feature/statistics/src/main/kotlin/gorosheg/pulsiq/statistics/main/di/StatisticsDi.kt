package gorosheg.pulsiq.statistics.main.di

import gorosheg.pulsiq.statistics.main.navigation.StatisticsScreenProvider
import gorosheg.pulsiq.statistics.main.presentation.StatisticsViewModel
import gorosheg.pulsiq.statistics.main.ui.StatisticsScreen
import gorosheg.pulsiq.statistics.main.ui.mapper.StatisticsUiStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val statisticsModule = module {
    factory { StatisticsScreenProvider(::StatisticsScreen) }

    viewModel {
        StatisticsViewModel(
            statisticsRepository = get(),
            navigator = get(),
            trackingSessionScreenProvider = get(),
            context = get(),
            uiStateMapper = StatisticsUiStateMapper()
        )
    }
}