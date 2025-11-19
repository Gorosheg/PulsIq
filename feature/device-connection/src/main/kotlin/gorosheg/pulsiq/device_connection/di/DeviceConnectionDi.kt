package gorosheg.pulsiq.device_connection.di

import gorosheg.pulsiq.common.navigation.provider.DeviceConnectionScreenProvider
import gorosheg.pulsiq.device_connection.presentation.DeviceConnectionViewModel
import gorosheg.pulsiq.device_connection.ui.DeviceConnectionScreen
import gorosheg.pulsiq.device_connection.ui.mapper.DeviceConnectionUiStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val deviceConnectionModule = module {
    factory { DeviceConnectionScreenProvider(::DeviceConnectionScreen) }

    viewModel {
        DeviceConnectionViewModel(
            bluetoothRepository = get(),
            uiStateMapper = DeviceConnectionUiStateMapper()
        )
    }
}