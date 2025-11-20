package gorosheg.pulsiq.pulse_notification

import android.R
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.app.ServiceCompat
import gorosheg.pulsiq.pulse_notification.PulseNotificationService.Companion.CHANNEL_ID
import gorosheg.pulsiq.pulse_notification.PulseNotificationService.Companion.NOTIF_ID

internal class NotificationBuilder(private val context: Context) {

    private val remoteViews by lazy {
        RemoteViews(
            context.packageName,
            gorosheg.pulsiq.pulse_notification.R.layout.notification_pulse
        )
    }

    fun build(): Notification {
        val dismissIntent = Intent(context, NotificationDismissedReceiver::class.java)
        val deletePendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val launchIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationBuilder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_menu_info_details)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setDeleteIntent(deletePendingIntent)
            .setContentIntent(pendingIntent)

        return notificationBuilder.build()
    }

    fun buildNotificationText(bpm: Int) {
        remoteViews.setTextViewText(
            gorosheg.pulsiq.pulse_notification.R.id.pulseText,
            context.getString(gorosheg.pulsiq.pulse_notification.R.string.bpm, bpm)
        )
    }

    fun Context.startForegroundCompat() {
        if (this !is Service) return

        val fgType =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                ServiceInfo.FOREGROUND_SERVICE_TYPE_HEALTH or ServiceInfo.FOREGROUND_SERVICE_TYPE_CONNECTED_DEVICE
            } else 0

        ServiceCompat.startForeground(
            this,
            NOTIF_ID,
            build(),
            fgType
        )
    }
}