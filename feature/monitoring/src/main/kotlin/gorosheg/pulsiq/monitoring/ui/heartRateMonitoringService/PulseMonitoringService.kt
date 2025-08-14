package gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import gorosheg.pulsiq.monitoring.R
import gorosheg.pulsiq.monitoring.presentation.PulseAlertController
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.get

class PulseMonitoringService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private lateinit var notificationManager: NotificationManager
    private val heartRateDevice by lazy { get<HeartRateDevice>() }
    private lateinit var pulseAlertController: PulseAlertController
    private lateinit var remoteViews: RemoteViews
    private lateinit var notificationBuilder: NotificationCompat.Builder
    private var vibratorPermission: Boolean = false
    private val appEnabledProvider: AppEnabledProvider by lazy { get() }

    override fun onCreate() {
        super.onCreate()
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val thresholdsRepository: ThresholdsRepository = get()
        pulseAlertController = PulseAlertController(
            context = applicationContext,
            thresholdsRepository = thresholdsRepository,
            scope = serviceScope
        )
        remoteViews = RemoteViews(packageName, R.layout.notification_pulse)
        vibratorPermission =
            ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) == PackageManager.PERMISSION_GRANTED

        createNotificationChannel()

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
        serviceScope.launch {
            heartRateDevice.heartRateFlow.collectLatest { bpm ->
                remoteViews.setTextViewText(R.id.pulseText, "$bpm bpm")
                notificationManager.notify(NOTIF_ID, notificationBuilder.build())

                if (vibratorPermission) {
                    pulseAlertController.onPulseChanged(bpm)
                }
            }
        }

        return START_STICKY
    }

    override fun onDestroy() {
        if (!appEnabledProvider.isAppEnabled) {
            heartRateDevice.disconnect()
        }
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
            val intent = Intent(context, PulseMonitoringService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, PulseMonitoringService::class.java))
        }
    }
}

