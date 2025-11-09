package gorosheg.pulsiq.settings.ui.mapper

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.settings.presentation.model.SettingsState
import gorosheg.pulsiq.settings.ui.model.SettingsUiState

internal class SettingsUiStateMapper : UiStateMapper<SettingsState, SettingsUiState> {
    override fun SettingsState.map(): SettingsUiState {
        return SettingsUiState(
            settingItems = buildSettingItems(
                lowerThreshold = lowerThreshold,
                upperThreshold = upperThreshold,
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled,
            ),
            selectedSettingItem = selectedSettingItem
        )
    }

    private fun buildSettingItems(
        lowerThreshold: Int,
        upperThreshold: Int,
        soundEnabled: Boolean,
        vibrationEnabled: Boolean
    ): List<SettingsUiState.SettingItem> {
        return listOf(
            SettingsUiState.SettingItem.ThresholdSettings(
                lowerThreshold = lowerThreshold,
                upperThreshold = upperThreshold,
                minimumThresholdText = R.string.min_threshold_title,
                maximumThresholdText = R.string.max_threshold_title
            ),
            SettingsUiState.SettingItem.SoundVibration(
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled,
                soundSwitchText = R.string.sound,
                vibrationSwitchText = R.string.vibration
            ),
            SettingsUiState.SettingItem.DeviceConnection
        )
    }
}