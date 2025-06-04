package gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationDismissedReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        HeartRateMonitoringService.stop(context)
    }
}
