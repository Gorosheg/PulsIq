package gorosheg.pulsiq.settings.ui.model

internal data class SettingsUiState(
    val settingItems: List<SettingItem> = emptyList(),
    val lowerThreshold: Int = 0,
    val upperThreshold: Int = 0
) {
    data class SettingItem(
        val id: Int,
        val title: String,
    )
}