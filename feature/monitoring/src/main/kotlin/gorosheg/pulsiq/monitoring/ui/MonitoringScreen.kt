package gorosheg.pulsiq.monitoring.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.monitoring.presentation.MonitoringViewModel
import org.koin.androidx.compose.getViewModel

internal class MonitoringScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<MonitoringViewModel>()

        MonitoringScreenContent()
    }
}