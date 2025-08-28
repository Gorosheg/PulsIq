package gorosheg.pulsiq.settings.di

import gorosheg.pulsiq.settings.navigation.SettingsScreenProvider
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.SettingsScreen
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsScreenProvider(::SettingsScreen) }

    viewModelOf(::SettingsViewModel)
}