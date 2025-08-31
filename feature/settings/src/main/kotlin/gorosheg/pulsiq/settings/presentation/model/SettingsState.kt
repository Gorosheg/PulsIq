package gorosheg.pulsiq.settings.presentation.model

import gorosheg.pulsiq.settings.ui.model.SettingsUiState.SettingItem

internal data class SettingsState(
	val settingItems: List<SettingItem> = emptyList(),
	val lowerThreshold: Int = 0,
	val upperThreshold: Int = 0
)