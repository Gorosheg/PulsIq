package gorosheg.pulsiq.monitoring.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.common.notification.PulseNotificationInitializer
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringEffect
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.MonitoringUiStateMapper
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import gorosheg.pulsiq.statistics_repository.StatisticsRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class MonitoringViewModel(
    private val bluetoothRepository: BluetoothRepository,
    private val pulseNotificationInitializer: PulseNotificationInitializer,
    private val statisticsRepository: StatisticsRepository,
) : BaseViewModel<MonitoringState, MonitoringUiState, MonitoringEffect>(
    initState = MonitoringState(),
    uiStateMapper = MonitoringUiStateMapper()
) {
    private var heartBeatSubscriptionJob: Job? = null

    fun startMonitoring() {
        viewModelScope.launch {
            statisticsRepository.createStatisticsSession()
        }
        pulseNotificationInitializer.startPulseNotification()
        subscribeToPulse()
        updateState { copy(isTracking = true) }
    }

    fun stopMonitoring() {
        pulseNotificationInitializer.stopPulseNotification()
        heartBeatSubscriptionJob?.cancel()
        heartBeatSubscriptionJob = null
        updateState { copy(isTracking = false, pulse = 0) }
        viewModelScope.launch {
            statisticsRepository.stopStatisticsSession()
        }
    }

    private fun subscribeToPulse() {
        heartBeatSubscriptionJob?.cancel()
        heartBeatSubscriptionJob = bluetoothRepository.heartRateFlow
            .onEach { pulse ->
                updateState { copy(pulse = pulse) }
                statisticsRepository.addPulse(pulse)
            }
            .launchIn(viewModelScope)
    }
}
