package gorosheg.pulsiq.settings.presentation

import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.settings.presentation.model.SettingsEffect
import gorosheg.pulsiq.settings.presentation.model.SettingsState
import gorosheg.pulsiq.settings.ui.SettingsUiStateMapper
import gorosheg.pulsiq.settings.ui.model.SettingsUiState

internal class SettingsViewModel : BaseViewModel<SettingsState, SettingsUiState, SettingsEffect>(
    initState = SettingsState(),
    uiStateMapper = SettingsUiStateMapper()
) {
    init {
        state { copy(settingItems = listOf(SettingsUiState.SettingItem(id = 0, title = "Границы пульса"))) }
    }
}