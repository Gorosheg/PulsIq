package gorosheg.pulsiq.monitoring.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface MonitoringScreenProvider {

    operator fun invoke(): Screen
}