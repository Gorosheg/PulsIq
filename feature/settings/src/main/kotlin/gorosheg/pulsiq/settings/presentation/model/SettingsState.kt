package gorosheg.pulsiq.settings.presentation.model

import gorosheg.pulsiq.settings.ui.model.SettingsUiState.SettingItem

internal data class SettingsState(
	val lowerThreshold: Int = 0,
	val upperThreshold: Int = 0,
	val soundEnabled: Boolean = true,
	val vibrationEnabled: Boolean = true,
	val selectedSettingItem: SettingItem? = null,
)