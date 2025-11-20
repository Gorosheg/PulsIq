package gorosheg.pulsiq.monitoring.di

import gorosheg.pulsiq.monitoring.navigation.MonitoringScreenProvider
import gorosheg.pulsiq.monitoring.presentation.MonitoringViewModel
import gorosheg.pulsiq.monitoring.ui.MonitoringScreen
import gorosheg.pulsiq.monitoring.ui.mapper.MonitoringUiStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val monitoringModule = module {
    factory { MonitoringScreenProvider(::MonitoringScreen) }

    viewModel {
        MonitoringViewModel(
            bluetoothRepository = get(),
            pulseNotificationInitializer = get(),
            statisticsRepository = get(),
            uiStateMapper = MonitoringUiStateMapper(get())
        )
    }
}