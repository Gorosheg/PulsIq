package gorosheg.pulsiq.statistics.tracking_session.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.statistics.tracking_session.presentation.TrackingSessionViewModel
import org.koin.androidx.compose.koinViewModel

internal class TrackingSessionScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: TrackingSessionViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()

        TrackingSessionScreenContent(
            state = state,
        )
    }
}