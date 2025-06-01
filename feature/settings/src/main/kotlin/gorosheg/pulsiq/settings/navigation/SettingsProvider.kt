package gorosheg.pulsiq.settings.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface SettingsProvider {
    operator fun invoke(): Screen
}