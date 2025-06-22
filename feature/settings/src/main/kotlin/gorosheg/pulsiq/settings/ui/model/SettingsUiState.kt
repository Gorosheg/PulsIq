package gorosheg.pulsiq.settings.ui.model

internal data class SettingsUiState(
    val settingItems: List<SettingItem> = emptyList(),
) {
    data class SettingItem(
        val id: Int,
        val title: String,
    )
}