package gorosheg.pulsiq.navigation.tabs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabOptions
import gorosheg.pulsiq.R
import gorosheg.pulsiq.monitoring.navigation.MonitoringScreenProvider
import org.koin.compose.koinInject

object MonitoringTab : Tab {
    @Suppress("unused")
    private fun readResolve(): Any = MonitoringTab

    override val options: TabOptions
        @Composable
        get() {
            val title = stringResource(R.string.MonitoringTabTitle)
            val icon = rememberVectorPainter(Icons.Default.Favorite)

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
        val screenProvider: MonitoringScreenProvider = koinInject<MonitoringScreenProvider>()

        SetNavigator(screenProvider())
    }
}