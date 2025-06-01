package gorosheg.pulsiq.statistics.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface StatisticsProvider {
    operator fun invoke(): Screen
}