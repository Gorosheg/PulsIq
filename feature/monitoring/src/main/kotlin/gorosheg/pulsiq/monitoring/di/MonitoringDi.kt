package gorosheg.pulsiq.monitoring.di

import gorosheg.pulsiq.monitoring.navigation.MonitoringScreenProvider
import gorosheg.pulsiq.monitoring.presentation.MonitoringViewModel
import gorosheg.pulsiq.monitoring.ui.MonitoringScreen
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val monitoringModule = module {
    factory { MonitoringScreenProvider(::MonitoringScreen) }

    viewModelOf(::MonitoringViewModel)
}