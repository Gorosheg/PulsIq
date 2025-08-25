package gorosheg.pulsiq.navigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import gorosheg.pulsiq.R
import gorosheg.pulsiq.statistics.navigation.StatisticsProvider
import org.koin.compose.koinInject

object StatisticsTab : Tab {
    private fun readResolve(): Any = StatisticsTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.StatisticsTabTitle)
            val icon = rememberVectorPainter(Icons.Default.Menu)

            return remember {
                TabOptions(
                    index = 0u,
                    title = title,
                    icon = icon
                )
            }
        }

    @Composable
    override fun Content() {
        val screenProvider: StatisticsProvider = koinInject<StatisticsProvider>()

        SetNavigator(screenProvider())
    }
}