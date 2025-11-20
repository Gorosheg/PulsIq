package gorosheg.pulsiq.statistics.main.di

import gorosheg.pulsiq.common.navigation.provider.TrackingSessionScreenProvider
import gorosheg.pulsiq.statistics.main.navigation.StatisticsScreenProvider
import gorosheg.pulsiq.statistics.main.presentation.StatisticsViewModel
import gorosheg.pulsiq.statistics.main.ui.StatisticsScreen
import gorosheg.pulsiq.statistics.main.ui.mapper.StatisticsUiStateMapper
import gorosheg.pulsiq.statistics.main.ui.mapper.UiPulseStatisticGroupBuilder
import gorosheg.pulsiq.statistics.tracking_session.presentation.TrackingSessionViewModel
import gorosheg.pulsiq.statistics.tracking_session.ui.TrackingSessionScreen
import gorosheg.pulsiq.statistics.tracking_session.ui.mapper.TrackingSessionUiStateMapper
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.util.Locale

val statisticsModule = module {
    factory { StatisticsScreenProvider(::StatisticsScreen) }
    factory { TrackingSessionScreenProvider { trackingSessionId -> TrackingSessionScreen(trackingSessionId) } }

    viewModel {
        StatisticsViewModel(
            statisticsRepository = get(),
            navigator = get(),
            trackingSessionScreenProvider = get(),
            uiStateMapper = StatisticsUiStateMapper(get())
        )
    }

    factory { UiPulseStatisticGroupBuilder(androidContext(), Locale.forLanguageTag("ru")) }

    viewModel {
        TrackingSessionViewModel(
            statisticsRepository = get(),
            uiStateMapper = TrackingSessionUiStateMapper()
        )
    }
}