package gorosheg.pulsiq.device_connection.di

import gorosheg.pulsiq.device_connection.navigation.DeviceConnectionScreenProvider
import gorosheg.pulsiq.device_connection.presentation.DeviceConnectionViewModel
import gorosheg.pulsiq.device_connection.ui.DeviceConnectionScreen
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val deviceConnectionModule = module {
    factory { DeviceConnectionScreenProvider(::DeviceConnectionScreen) }

    viewModelOf(::DeviceConnectionViewModel)
}