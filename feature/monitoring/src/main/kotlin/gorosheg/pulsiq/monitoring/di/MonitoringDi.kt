package gorosheg.pulsiq.monitoring.di

import gorosheg.pulsiq.monitoring.navigation.MonitoringProvider
import gorosheg.pulsiq.monitoring.presentation.MonitoringViewModel
import gorosheg.pulsiq.monitoring.ui.MonitoringScreen
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val monitoringModule = module {

    factory { MonitoringProvider(::MonitoringScreen) }

    viewModel {
        MonitoringViewModel(
            heartBeatDataSource = get(),
            thresholdsRepository = get(),
            pulseNotificationInitializer = get()
        )
    }
}