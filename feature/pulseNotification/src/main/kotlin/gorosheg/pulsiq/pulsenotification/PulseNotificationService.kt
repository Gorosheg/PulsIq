package gorosheg.pulsiq.pulsenotification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.navigation.AppEnabledProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

internal class PulseNotificationService : Service() {

    private val heartBeatDataSource: HeartBeatDataSource by inject()
    private val appEnabledProvider: AppEnabledProvider by inject()
    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val remoteViews by lazy { RemoteViews(packageName, R.layout.notification_pulse) }
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()

        createNotificationChannel()
        buildNotification()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIF_ID, notificationBuilder.build())
        serviceScope.launch {
            heartBeatDataSource.heartRateFlow.collectLatest { bpm ->
                remoteViews.setTextViewText(R.id.pulseText, getString(R.string.bpm, bpm))
                notificationManager.notify(NOTIF_ID, notificationBuilder.build())
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        if (!appEnabledProvider.isAppEnabled) {
            heartBeatDataSource.disconnect()
        }
        serviceScope.cancel()

        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            lockscreenVisibility = Notification.VISIBILITY_PUBLIC
        }
        notificationManager.createNotificationChannel(channel)
    }

    private fun buildNotification() {
        val dismissIntent = Intent(this, NotificationDismissedReceiver::class.java)
        val deletePendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            dismissIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            launchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setOnlyAlertOnce(true)
            .setCustomContentView(remoteViews)
            .setCustomBigContentView(remoteViews)
            .setDeleteIntent(deletePendingIntent)
            .setContentIntent(pendingIntent)
    }

    companion object {
        private const val CHANNEL_ID = "pulse_monitor_channel"
        private const val CHANNEL_NAME = "pulse Monitoring"
        private const val NOTIF_ID = 1

        fun start(context: Context) {
            val intent = Intent(context, PulseNotificationService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, PulseNotificationService::class.java))
        }
    }
}