package gorosheg.pulsiq.settings.presentation

import com.example.storage.settings.SettingsRepository
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.settings.presentation.model.SettingsEffect
import gorosheg.pulsiq.settings.presentation.model.SettingsState
import gorosheg.pulsiq.settings.ui.SettingsUiStateMapper
import gorosheg.pulsiq.settings.ui.model.SettingsUiState

internal class SettingsViewModel(
    val settingsRepository: SettingsRepository
) : BaseViewModel<SettingsState, SettingsUiState, SettingsEffect>(
    initState = SettingsState(),
    uiStateMapper = SettingsUiStateMapper()
) {
    init {
        state {
            copy(
                settingItems = listOf(
                    SettingsUiState.SettingItem(id = 0, title = "Границы пульса"),
                    SettingsUiState.SettingItem(id = 1, title = "Звук и вибрация")
                ),
                lowerThreshold = settingsRepository.getLowerThreshold(),
                upperThreshold = settingsRepository.getUpperThreshold(),
                soundEnabled = settingsRepository.getSoundEnabled(),
                vibrationEnabled = settingsRepository.getVibrationEnabled()
            )
        }
    }

    fun updateThresholds(lower: Int, upper: Int) {
        state { copy(lowerThreshold = lower, upperThreshold = upper) }
        settingsRepository.saveThresholds(lower = lower, upper = upper)
    }

    fun updateSoundVibration(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        state { copy(soundEnabled = soundEnabled, vibrationEnabled = vibrationEnabled) }
        settingsRepository.saveSoundVibration(soundEnabled = soundEnabled, vibrationEnabled = vibrationEnabled)
    }
}