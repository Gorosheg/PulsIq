package gorosheg.pulsiq.settings.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.common.navigation.NavigatorHolder
import gorosheg.pulsiq.common.navigation.provider.DeviceConnectionScreenProvider
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.settings.presentation.model.SettingsState
import gorosheg.pulsiq.settings.ui.SettingsUiStateMapper
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import gorosheg.pulsiq.storage.settings.SettingsRepository
import kotlinx.coroutines.launch

internal class SettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val navigator: NavigatorHolder,
    private val deviceConnectionScreenProvider: DeviceConnectionScreenProvider,
) : BaseViewModel<SettingsState, SettingsUiState>(
    initState = SettingsState(),
    uiStateMapper = SettingsUiStateMapper()
) {
    init {
        viewModelScope.launch {
            val lowerThreshold = settingsRepository.getLowerThreshold()
            val upperThreshold = settingsRepository.getUpperThreshold()
            val soundEnabled = settingsRepository.getSoundEnabled()
            val vibrationEnabled = settingsRepository.getVibrationEnabled()
            updateState {
                copy(
                    lowerThreshold = lowerThreshold,
                    upperThreshold = upperThreshold,
                    soundEnabled = soundEnabled,
                    vibrationEnabled = vibrationEnabled,
                )
            }
        }

    }

    fun updateThresholds(lower: Int, upper: Int) {
        updateState { copy(lowerThreshold = lower, upperThreshold = upper) }
        viewModelScope.launch {
            settingsRepository.saveThresholds(lower = lower, upper = upper)
        }
    }

    fun updateSoundVibration(soundEnabled: Boolean, vibrationEnabled: Boolean) {
        updateState { copy(soundEnabled = soundEnabled, vibrationEnabled = vibrationEnabled) }
        viewModelScope.launch {
            settingsRepository.saveSoundVibration(
                soundEnabled = soundEnabled,
                vibrationEnabled = vibrationEnabled
            )
        }
    }

    fun navigateToDetailsScreen() {
        navigator.navigator?.push(deviceConnectionScreenProvider())
    }
}