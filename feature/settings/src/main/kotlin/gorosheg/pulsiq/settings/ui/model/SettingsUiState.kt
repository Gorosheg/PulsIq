package gorosheg.pulsiq.settings.ui.model

import gorosheg.pulsiq.settings.R

internal data class SettingsUiState(
    val settingItems: List<SettingItem> = emptyList(),
) {
    sealed class SettingItem(internal val title: Int) {

        class ThresholdSettings(
            val lowerThreshold: Int = 0,
            val upperThreshold: Int = 0,
            val minimumThresholdText: Int,
            val maximumThresholdText: Int,
        ) : SettingItem(R.string.pulseThresholds)

        class SoundVibration(
            val soundEnabled: Boolean = true,
            val vibrationEnabled: Boolean = true,
            val soundSwitchText: Int,
            val vibrationSwitchText: Int,
        ) : SettingItem(R.string.soundVibration)

        object DeviceConnection : SettingItem(R.string.findDevices)
    }
}