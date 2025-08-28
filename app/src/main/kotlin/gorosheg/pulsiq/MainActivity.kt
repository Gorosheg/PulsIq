package gorosheg.pulsiq


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import gorosheg.pulsiq.navigation.tabs.MonitoringTab
import gorosheg.pulsiq.navigation.tabs.SettingsTab
import gorosheg.pulsiq.navigation.tabs.StatisticsTab
import gorosheg.pulsiq.ui.Blue
import gorosheg.pulsiq.ui.MyAppTheme
import gorosheg.pulsiq.ui.White

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Content()
            }
        }
    }

    @Composable
    fun Content() {
        TabNavigator(MonitoringTab) {
            Scaffold(
                content = { padding ->
                    Row(modifier = Modifier.padding(padding)) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    BottomAppBar {
                        TabNavigationItem(MonitoringTab)
                        TabNavigationItem(StatisticsTab)
                        TabNavigationItem(SettingsTab)
                    }
                }
            )
        }
    }

    @Composable
    private fun RowScope.TabNavigationItem(tab: Tab) {
        val tabNavigator = LocalTabNavigator.current
        val selected = tabNavigator.current.key == tab.key
        val contentColor = if (selected) Blue else White

        NavigationBarItem(
            selected = selected,
            onClick = { tabNavigator.current = tab },
            icon = {
                Icon(
                    painter = requireNotNull(tab.options.icon),
                    contentDescription = tab.options.title
                )
            },
            label = { Text(text = tab.options.title) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = contentColor,
                selectedTextColor = contentColor,
                unselectedIconColor = contentColor.copy(alpha = 0.7f),
                unselectedTextColor = contentColor.copy(alpha = 0.7f),
                indicatorColor = Color.Transparent
            )
        )
    }
}