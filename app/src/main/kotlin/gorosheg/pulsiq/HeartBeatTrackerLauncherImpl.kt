package gorosheg.pulsiq

import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.common.activity_running_checker.HeartBeatTrackerLauncher
import gorosheg.pulsiq.statistics_repository.StatisticsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class HeartBeatTrackerLauncherImpl(
    private val heartBeatDataSource: BluetoothRepository,
    private val statisticsRepository: StatisticsRepository,
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
            if (shouldDisconnect) {
                heartBeatDataSource.disconnect()
                statisticsRepository.stopStatisticsSession()
            }
        }.launchIn(scope)
    }

    override fun changeActivityState(isRunning: Boolean) {
        isActivityRunning.value = isRunning
    }

    override fun changeServiceState(isRunning: Boolean) {
        isNotificationServiceRunning.value = isRunning
    }
}