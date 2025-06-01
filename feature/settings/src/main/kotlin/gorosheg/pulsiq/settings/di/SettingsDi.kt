package gorosheg.pulsiq.settings.di

import gorosheg.pulsiq.settings.navigation.SettingsProvider
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.SettingsScreen
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    factory { SettingsProvider(::SettingsScreen) }
    viewModel { SettingsViewModel() }
}