package gorosheg.pulsiq.device_connection.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.device_connection.presentation.DeviceConnectionViewModel
import org.koin.androidx.compose.koinViewModel

internal class DeviceConnectionScreen : Screen {
    @Composable
    override fun Content() {
        val viewModel: DeviceConnectionViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()

        DeviceConnectionScreenContent(
            state = state,
            onStartScan = { viewModel.startScan() },
            onStopScan = { viewModel.stopScan() },
            onDeviceClick = { address -> viewModel.onDeviceClick(address) },
            onDisconnect = { viewModel.disconnect() }
        )
    }
}