package gorosheg.pulsiq.common.navigation.provider

import cafe.adriel.voyager.core.screen.Screen

fun interface DeviceConnectionScreenProvider {

    operator fun invoke(): Screen
}