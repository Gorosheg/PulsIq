package gorosheg.pulsiq.monitoring.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.notification.PulseNotificationInitializer
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringEffect
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.MonitoringUiStateMapper
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MonitoringViewModel(
    private val heartBeatDataSource: HeartBeatDataSource,
    private val pulseNotificationInitializer: PulseNotificationInitializer,
) : BaseViewModel<MonitoringState, MonitoringUiState, MonitoringEffect>(
    initState = MonitoringState(),
    uiStateMapper = MonitoringUiStateMapper()
) {
    private var heartBeatSubscriptionJob: Job? = null

    fun startMonitoring() {
        pulseNotificationInitializer.startPulseNotification()
        subscribeToPulse()
        state { copy(isTracking = true) }
    }

    fun stopMonitoring() {
        pulseNotificationInitializer.stopPulseNotification()
        heartBeatSubscriptionJob?.cancel()
        heartBeatSubscriptionJob = null
        state { copy(isTracking = false, pulse = 0) }
    }

    private fun subscribeToPulse() {
        heartBeatSubscriptionJob?.cancel()
        heartBeatSubscriptionJob = heartBeatDataSource.subscribeHeartRateFlow()
            .onEach { state { copy(pulse = it) } }
            .launchIn(viewModelScope)
    }
}
