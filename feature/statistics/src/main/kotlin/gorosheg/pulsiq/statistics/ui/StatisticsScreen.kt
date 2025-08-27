package gorosheg.pulsiq.statistics.ui

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import gorosheg.pulsiq.statistics.presentation.StatisticsViewModel
import org.koin.androidx.compose.koinViewModel

internal class StatisticsScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: StatisticsViewModel = koinViewModel()

        StatisticsScreenContent()
    }
}