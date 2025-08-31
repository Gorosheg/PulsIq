package gorosheg.pulsiq.settings.presentation

import com.example.storage.ThresholdsRepository
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.settings.presentation.model.SettingsEffect
import gorosheg.pulsiq.settings.presentation.model.SettingsState
import gorosheg.pulsiq.settings.ui.SettingsUiStateMapper
import gorosheg.pulsiq.settings.ui.model.SettingsUiState

internal class SettingsViewModel(
    val thresholdsRepository: ThresholdsRepository
) : BaseViewModel<SettingsState, SettingsUiState, SettingsEffect>(
    initState = SettingsState(),
    uiStateMapper = SettingsUiStateMapper()
) {
    init {
        state {
            copy(
                settingItems = listOf(SettingsUiState.SettingItem(id = 0, title = "Границы пульса")),
                lowerThreshold = thresholdsRepository.getLowerThreshold(),
                upperThreshold = thresholdsRepository.getUpperThreshold()
            )
        }
    }

    fun updateThresholds(lower: Int, upper: Int) {
        state { copy(lowerThreshold = lower, upperThreshold = upper) }
        thresholdsRepository.saveThresholds(lower = lower, upper = upper)
    }
}