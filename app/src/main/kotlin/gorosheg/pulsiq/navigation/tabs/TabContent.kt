package gorosheg.pulsiq.navigation.tabs

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import gorosheg.pulsiq.common.navigation.NavigatorHolder
import org.koin.compose.koinInject

@Composable
internal fun SetNavigator(screen: Screen) {
    Navigator(
        screen = screen,
    ) { navigator ->
        koinInject<NavigatorHolder>().navigator = navigator

        FadeTransition(navigator) { screen ->
            screen.Content()
        }
    }
}