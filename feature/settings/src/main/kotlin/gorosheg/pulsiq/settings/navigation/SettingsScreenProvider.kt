package gorosheg.pulsiq.settings.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface SettingsScreenProvider {

    operator fun invoke(): Screen
}