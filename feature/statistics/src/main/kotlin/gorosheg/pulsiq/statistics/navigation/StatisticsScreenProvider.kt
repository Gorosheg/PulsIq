package gorosheg.pulsiq.statistics.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface StatisticsScreenProvider {

    operator fun invoke(): Screen
}