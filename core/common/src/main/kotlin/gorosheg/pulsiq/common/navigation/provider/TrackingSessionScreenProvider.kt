package gorosheg.pulsiq.common.navigation.provider

import cafe.adriel.voyager.core.screen.Screen

fun interface TrackingSessionScreenProvider {

    operator fun invoke(trackingSessionId: Int): Screen
}