package gorosheg.pulsiq.statistics.main.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.statistics.main.presentation.StatisticsViewModel
import org.koin.androidx.compose.koinViewModel

internal class StatisticsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: StatisticsViewModel = koinViewModel()
        val state by viewModel.uiState.collectAsState()

        StatisticsScreenContent(
            state = state,
            onTrackingSessionSwipe = viewModel::removeTrackingSession
        )
    }
}