package gorosheg.pulsiq.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import org.koin.androidx.compose.koinViewModel

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SettingsViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()
        val selectedSetting = remember { mutableStateOf<SettingsUiState.SettingItem?>(null) }

        SettingsScreenContent(
            state = state,
            onSettingClick = { id ->
                val setting = state.settingItems.firstOrNull { it.id == id }
                selectedSetting.value = setting
            }
        )

        selectedSetting.value?.let { setting ->
            when (setting.id) {
                0 -> {
                    PulseThresholdDialog(
                        lowerThreshold = state.lowerThreshold,
                        upperThreshold = state.upperThreshold,
                        onDismiss = { selectedSetting.value = null },
                        onApply = { lower, upper ->
                            viewModel.updateThresholds(lower = lower, upper = upper)
                            selectedSetting.value = null
                        }
                    )
                }

                1 -> {
                    SoundVibrationDialog(
                        soundEnabled = state.soundEnabled,
                        vibrationEnabled = state.vibrationEnabled,
                        onDismiss = { selectedSetting.value = null },
                        onApply = { sound, vibration ->
                            viewModel.updateSoundVibration(soundEnabled = sound, vibrationEnabled = vibration)
                            selectedSetting.value = null
                        }
                    )
                }

                2 -> viewModel.navigateToDetailsScreen()
            }
        }
    }
}
