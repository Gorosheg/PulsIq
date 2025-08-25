package gorosheg.pulsiq.monitoring.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface MonitoringProvider {

    operator fun invoke(): Screen
}