package gorosheg.pulsiq.statistics.tracking_session.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.statistics.tracking_session.presentation.TrackingSessionViewModel
import org.koin.androidx.compose.koinViewModel

internal class TrackingSessionScreen(
    private val trackingSessionId: Int
) : Screen {

    @Composable
    override fun Content() {
        val viewModel: TrackingSessionViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()

        LaunchedEffect(trackingSessionId) {
            viewModel.getTrackingSession(trackingSessionId)
        }

        TrackingSessionScreenContent(
            state = state,
            onEditClick = { viewModel.onEditClick() },
            onCloseEditDialogClick = { viewModel.closeEditDialog() },
            onNameChanged = { viewModel.changeName(it) }
        )
    }
}