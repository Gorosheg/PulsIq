package gorosheg.pulsiq.settings.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.settings.R
import gorosheg.pulsiq.settings.ui.components.PulseThresholdDialog
import gorosheg.pulsiq.settings.ui.components.SettingCard
import gorosheg.pulsiq.settings.ui.components.SoundVibrationDialog
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import gorosheg.pulsiq.ui.MyAppTheme

@Composable
internal fun SettingsScreenContent(
    state: SettingsUiState,
    onSettingClick: (setting: SettingsUiState.SettingItem) -> Unit,
    onSoundVibrationUpdate: (soundEnabled: Boolean, vibrationEnabled: Boolean) -> Unit,
    onThresholdsUpdate: (lower: Int, upper: Int) -> Unit,
    onSettingDismiss: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(state.settingItems) { setting ->
            SettingCard(
                settingTitle = setting.title,
                onClick = { onSettingClick(setting) }
            )
        }
    }

    when (state.selectedSettingItem) {
        is SettingsUiState.SettingItem.ThresholdSettings -> {
            PulseThresholdDialog(
                setting = state.selectedSettingItem,
                onApply = { lower, upper ->
                    onThresholdsUpdate(lower, upper)
                    onSettingDismiss.invoke()
                },
                onDismiss = onSettingDismiss::invoke
            )
        }

        is SettingsUiState.SettingItem.SoundVibration -> {
            SoundVibrationDialog(
                setting = state.selectedSettingItem,
                onApply = { sound, vibration ->
                    onSoundVibrationUpdate(sound, vibration)
                    onSettingDismiss.invoke()
                },
                onDismiss = onSettingDismiss::invoke
            )
        }

        else -> Unit
    }
}

@Preview()
@Composable
private fun SettingsScreenContentPreview() {
    MyAppTheme {
        SettingsScreenContent(
            state = SettingsUiState(
                settingItems = listOf(
                    SettingsUiState.SettingItem.ThresholdSettings(
                        lowerThreshold = 140,
                        upperThreshold = 180,
                        minimumThresholdText = R.string.min_threshold_title,
                        maximumThresholdText = R.string.max_threshold_title,
                    ),
                    SettingsUiState.SettingItem.SoundVibration(
                        soundEnabled = true,
                        vibrationEnabled = false,
                        soundSwitchText = R.string.sound,
                        vibrationSwitchText = R.string.vibration,
                    ),
                    SettingsUiState.SettingItem.DeviceConnection
                )
            ),
            onSettingClick = {},
            onSoundVibrationUpdate = { _, _ ->
            },
            onThresholdsUpdate = { _, _ ->
            },
            onSettingDismiss = {},
        )
    }
}
