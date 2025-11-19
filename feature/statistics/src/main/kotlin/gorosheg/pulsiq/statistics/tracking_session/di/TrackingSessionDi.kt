package gorosheg.pulsiq.statistics.tracking_session.di

import gorosheg.pulsiq.common.navigation.provider.TrackingSessionScreenProvider
import gorosheg.pulsiq.statistics.tracking_session.presentation.TrackingSessionViewModel
import gorosheg.pulsiq.statistics.tracking_session.ui.TrackingSessionScreen
import gorosheg.pulsiq.statistics.tracking_session.ui.mapper.TrackingSessionUiStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val trackingSessionModule = module {
    factory { TrackingSessionScreenProvider { trackingSessionId -> TrackingSessionScreen(trackingSessionId) } }

    viewModel {
        TrackingSessionViewModel(
            statisticsRepository = get(),
            uiStateMapper = TrackingSessionUiStateMapper()
        )
    }
}