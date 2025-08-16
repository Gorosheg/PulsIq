package gorosheg.pulsiq.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import gorosheg.pulsiq.settings.ui.model.SettingsUiState
import org.koin.androidx.compose.get
import org.koin.androidx.compose.getViewModel

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<SettingsViewModel>()
        val state by viewModel.uiState.collectAsState()
        val thresholdsRepository = get<ThresholdsRepository>()

        val lowerThreshold = remember { mutableIntStateOf(thresholdsRepository.getLowerThreshold()) }
        val upperThreshold = remember { mutableIntStateOf(thresholdsRepository.getUpperThreshold()) }
        val selectedSetting = remember { mutableStateOf<SettingsUiState.SettingItem?>(null) }

        SettingsScreenContent(
            state = state,
            onSettingClick = { id ->
                val setting = state.settingItems.firstOrNull { it.id == id }
                selectedSetting.value = setting
            }
        )

        selectedSetting.value?.let {
            PulseThresholdDialog(
                lowerThreshold = lowerThreshold,
                upperThreshold = upperThreshold,
                onDismiss = { selectedSetting.value = null },
                onApply = { lower, upper ->
                    thresholdsRepository.saveThresholds(lower = lower, upper = upper)
                    selectedSetting.value = null
                }
            )
        }
    }

}
