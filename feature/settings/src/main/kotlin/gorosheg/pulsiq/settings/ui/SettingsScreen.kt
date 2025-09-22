package gorosheg.pulsiq.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import org.koin.androidx.compose.koinViewModel

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SettingsViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()
        var selectedSetting by remember { mutableStateOf<SettingsUiState.SettingItem?>(null) }

        SettingsScreenContent(
            state = state,
            onSettingClick = { setting ->
                selectedSetting = setting
            }
        )

        selectedSetting?.let { setting ->
            when (setting) {
                is SettingsUiState.SettingItem.ThresholdSettings -> {
                    PulseThresholdDialog(
                        setting = setting,
                        onApply = { lower, upper ->
                            viewModel.updateThresholds(
                                lower = lower,
                                upper = upper
                            )
                            selectedSetting = null
                        },
                        onDismiss = { selectedSetting = null }
                    )
                }

                is SettingsUiState.SettingItem.SoundVibration -> {
                    SoundVibrationDialog(
                        setting = setting,
                        onApply = { sound, vibration ->
                            viewModel.updateSoundVibration(
                                soundEnabled = sound,
                                vibrationEnabled = vibration
                            )
                            selectedSetting = null
                        },
                        onDismiss = { selectedSetting = null }
                    )
                }

                is SettingsUiState.SettingItem.DeviceConnection -> {
                    viewModel.navigateToDetailsScreen()
                    selectedSetting = null
                }
            }
        }
    }

}
