package gorosheg.pulsiq.settings.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.koinViewModel

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: SettingsViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()

        SettingsScreenContent(
            state = state,
            onSettingClick = viewModel::onSettingClick,
            onSoundVibrationUpdate = viewModel::updateSoundVibration,
            onThresholdsUpdate = viewModel::updateThresholds,
            onSettingDismiss = viewModel::onSettingDismiss,
        )
    }
}
