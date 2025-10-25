package gorosheg.pulsiq.statistics.main.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface StatisticsScreenProvider {

    operator fun invoke(): Screen
}