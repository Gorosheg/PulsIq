package gorosheg.pulsiq.device_connection.navigation

import cafe.adriel.voyager.core.screen.Screen

fun interface DeviceConnectionScreenProvider {

    operator fun invoke(): Screen
}