package gorosheg.pulsiq.pulse_notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

internal class NotificationDismissedReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        PulseNotificationService.stop(context)
    }
}
