package gorosheg.pulsiq

import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.activity_running_checker.HeartBeatTrackerLauncher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class HeartBeatTrackerLauncherImpl( // todo: service неверно закрывается при дестрое приложения? HeartBeatTrackerLauncherImpl пересоздается?.
    private val heartBeatDataSource: HeartBeatDataSource,
    scope: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
) : HeartBeatTrackerLauncher {

    private val isActivityRunning = MutableStateFlow(true)
    private val isNotificationServiceRunning = MutableStateFlow(false)

    init {
        combine(
            isActivityRunning,
            isNotificationServiceRunning
        ) { activityRunning, serviceRunning ->
            !activityRunning && !serviceRunning
        }.onEach { shouldDisconnect ->
            println("srbbht activity: ${isActivityRunning.value}, service: ${isNotificationServiceRunning.value}")
            if (shouldDisconnect) {
                println("srbbht shouldDisconnect")
                heartBeatDataSource.disconnect()
            }
        }.launchIn(scope)
    }

    override fun changeActivityState(isRunning: Boolean) {
        println("srbbht changeActivityState: $isRunning")
        isActivityRunning.value = isRunning
    }

    override fun changeServiceState(isRunning: Boolean) {
        println("srbbht changeServiceState: $isRunning")
        isNotificationServiceRunning.value = isRunning
    }
}