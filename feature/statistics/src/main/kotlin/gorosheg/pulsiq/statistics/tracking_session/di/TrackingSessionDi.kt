package gorosheg.pulsiq.statistics.tracking_session.di

import gorosheg.pulsiq.common.navigation.provider.TrackingSessionScreenProvider
import gorosheg.pulsiq.statistics.tracking_session.presentation.TrackingSessionViewModel
import gorosheg.pulsiq.statistics.tracking_session.ui.TrackingSessionScreen
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val trackingSessionModule = module {
    factory { TrackingSessionScreenProvider { TrackingSessionScreen() } }

    viewModelOf(::TrackingSessionViewModel)
}