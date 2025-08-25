package gorosheg.pulsiq.monitoring.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.notification.PulseNotificationInitializer
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringEffect
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.MonitoringUiStateMapper
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MonitoringViewModel(
    private val heartBeatDataSource: HeartBeatDataSource,
    private val thresholdsRepository: ThresholdsRepository,
    private val pulseNotificationInitializer: PulseNotificationInitializer,
) : BaseViewModel<MonitoringState, MonitoringUiState, MonitoringEffect>(
    MonitoringState(),
    MonitoringUiStateMapper()
) {

    init {
        subscribeToPulse()
    }

    fun startMonitoring() {
        heartBeatDataSource.startScan()
        pulseNotificationInitializer.startPulseNotification()
        state { copy(isTracking = true) }
    }

    fun stopMonitoring() {
        heartBeatDataSource.disconnect()
        pulseNotificationInitializer.stopPulseNotification()
        state { copy(isTracking = false, pulse = 0) }
    }

    private fun subscribeToPulse() {
        heartBeatDataSource.heartRateFlow
            .onEach {
                state {
                    copy(pulse = it)
                }
            }
            .launchIn(viewModelScope)
    }
}
