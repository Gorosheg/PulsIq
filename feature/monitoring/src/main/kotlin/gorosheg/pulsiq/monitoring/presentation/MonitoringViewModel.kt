package gorosheg.pulsiq.monitoring.presentation

import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import gorosheg.pulsiq.common.navigation.PulseNotificationInitializer
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringEffect
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.MonitoringUiStateMapper
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MonitoringViewModel(
    private val heartRateDevice: HeartRateDevice,
    private val thresholdsRepository: ThresholdsRepository,
    private val pulseNotificationInitializer: PulseNotificationInitializer,
) : BaseViewModel<MonitoringState, MonitoringUiState, MonitoringEffect>(
    MonitoringState(),
    MonitoringUiStateMapper()
) {

    init {
        subscribeToPulse()
        subscribeToThresholds()
    }

    fun startMonitoring() {
        state { copy(isTracking = true) }
        heartRateDevice.startScan()
        pulseNotificationInitializer.startPulseNotification()
    }

    fun stopMonitoring() {
        state { copy(isTracking = false, pulse = 0) }
        heartRateDevice.disconnect()
        pulseNotificationInitializer.stopPulseNotification()
    }

    private fun subscribeToPulse() {
        heartRateDevice.heartRateFlow
            .onEach {
                state {
                    copy(pulse = it)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun subscribeToThresholds() {
        thresholdsRepository.lowerThresholdFlow
            .onEach { lower -> state { copy(lowerThreshold = lower / 100 * 70) } }
            .launchIn(viewModelScope)

        thresholdsRepository.upperThresholdFlow
            .onEach { upper -> state { copy(upperThreshold = upper / 100 * 80) } }
            .launchIn(viewModelScope)
    }
}
