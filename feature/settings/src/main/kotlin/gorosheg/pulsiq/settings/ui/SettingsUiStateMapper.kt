package gorosheg.pulsiq.settings.ui

import gorosheg.pulsiq.common.viewModel.uiStateMapper.UiStateMapper
import gorosheg.pulsiq.settings.presentation.model.SettingsState
import gorosheg.pulsiq.settings.ui.model.SettingsUiState

internal class SettingsUiStateMapper : UiStateMapper<SettingsState, SettingsUiState> {
    override fun SettingsState.map(): SettingsUiState {
        return SettingsUiState(
            settingItems = this.settingItems
        )
    }
}