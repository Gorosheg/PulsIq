package gorosheg.pulsiq.pulsenotification

import android.content.Context
import gorosheg.pulsiq.common.navigation.PulseNotificationInitializer

class PulseNotificationInitializerImpl(private val appContext: Context) : PulseNotificationInitializer {
    override fun startPulseNotification() {
        PulseNotificationService.start(appContext)
    }

    override fun stopPulseNotification() {
        PulseNotificationService.stop(appContext)
    }

}