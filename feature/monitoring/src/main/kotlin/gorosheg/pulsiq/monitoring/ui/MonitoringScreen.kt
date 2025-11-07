package gorosheg.pulsiq.monitoring.ui


import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import gorosheg.pulsiq.monitoring.presentation.MonitoringViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalPermissionsApi::class)
internal class MonitoringScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: MonitoringViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()


        MonitoringScreenContent(
            state = state,
            startTracking = viewModel::startMonitoring,
            stopTracking = viewModel::stopMonitoring,
            onSessionNameChanged = viewModel::changeSessionName,
            onNameDialogDismiss = viewModel::onNameDialogDismiss,
        )
    }
}