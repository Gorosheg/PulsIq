package gorosheg.pulsiq.settings.di

import gorosheg.pulsiq.settings.navigation.SettingsScreenProvider
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.SettingsScreen
import gorosheg.pulsiq.settings.ui.mapper.SettingsUiStateMapper
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsScreenProvider(::SettingsScreen) }

    viewModel {
        SettingsViewModel(
            settingsRepository = get(),
            navigator = get(),
            deviceConnectionScreenProvider = get(),
            uiStateMapper = SettingsUiStateMapper()
        )
    }
}