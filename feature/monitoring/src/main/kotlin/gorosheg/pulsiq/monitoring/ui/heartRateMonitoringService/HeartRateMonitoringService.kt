package gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import gorosheg.pulsiq.bluetooth.BleHeartRateDevice
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import gorosheg.pulsiq.monitoring.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class HeartRateMonitoringService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var heartRateDevice: HeartRateDevice
    private lateinit var notificationManager: NotificationManager
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private lateinit var remoteViews: RemoteViews

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        heartRateDevice = BleHeartRateDevice(applicationContext)
        createNotificationChannel()

        remoteViews = RemoteViews(packageName, R.layout.notification_pulse)

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

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIF_ID, notificationBuilder.build())
        heartRateDevice.startScan()

        serviceScope.launch {
            heartRateDevice.heartRateFlow.collectLatest { bpm ->
                remoteViews.setTextViewText(R.id.pulseText, "$bpm bpm")
                notificationManager.notify(NOTIF_ID, notificationBuilder.build())
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        heartRateDevice.disconnect()
        serviceScope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pulse Monitoring",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                lockscreenVisibility = Notification.VISIBILITY_PUBLIC
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        const val CHANNEL_ID = "pulse_monitor_channel"
        const val NOTIF_ID = 1

        fun start(context: Context) {
            val intent = Intent(context, HeartRateMonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, HeartRateMonitoringService::class.java))
        }
    }
}

