package gorosheg.pulsiq.pulsenotification

import android.content.Context
import gorosheg.pulsiq.common.notification.PulseNotificationInitializer

internal class PulseNotificationInitializerImpl(
    private val appContext: Context
) : PulseNotificationInitializer {

    override fun startPulseNotification() {
        PulseNotificationService.start(appContext)
    }

    override fun stopPulseNotification() {
        PulseNotificationService.stop(appContext)
    }
}