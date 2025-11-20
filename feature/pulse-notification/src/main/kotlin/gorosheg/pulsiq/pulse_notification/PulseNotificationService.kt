package gorosheg.pulsiq.pulse_notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.ServiceCompat
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.common.activity_running_checker.HeartBeatTrackerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

internal class PulseNotificationService : Service() {

    private val bluetoothRepository: BluetoothRepository by inject()
    private val heartBeatTrackerLauncher: HeartBeatTrackerLauncher by inject()
    private val notificationBuilder: NotificationBuilder by inject()

    private val notificationManager by lazy { getSystemService(NOTIFICATION_SERVICE) as NotificationManager }
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        heartBeatTrackerLauncher.changeServiceState(true)
        createNotificationChannel()

        scope.launch {
            bluetoothRepository.heartRateFlow.collectLatest { bpm ->
                notificationBuilder.buildNotificationText(bpm)
                notificationManager.notify(NOTIF_ID, notificationBuilder.build())
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        with(notificationBuilder) {
            this@PulseNotificationService.startForegroundCompat()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        heartBeatTrackerLauncher.changeServiceState(false)
        scope.cancel()
        ServiceCompat.stopForeground(this, ServiceCompat.STOP_FOREGROUND_REMOVE)
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

    companion object {
        private const val CHANNEL_NAME = "Pulse Monitoring"
        const val NOTIF_ID = 1
        const val CHANNEL_ID = "pulse_monitor_channel"

        fun start(context: Context) {
            val intent = Intent(context, PulseNotificationService::class.java)
            context.startForegroundService(intent)
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, PulseNotificationService::class.java))
        }
    }
}