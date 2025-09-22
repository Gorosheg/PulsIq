package gorosheg.pulsiq.settings.presentation.model

internal data class SettingsState(
	val lowerThreshold: Int = 0,
	val upperThreshold: Int = 0,
	val soundEnabled: Boolean = true,
	val vibrationEnabled: Boolean = true
)