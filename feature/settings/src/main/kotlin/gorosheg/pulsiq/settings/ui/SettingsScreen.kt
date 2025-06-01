package gorosheg.pulsiq.settings.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.settings.presentation.SettingsViewModel
import org.koin.androidx.compose.getViewModel

internal class SettingsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<SettingsViewModel>()

        SettingsScreenContent()
    }
}