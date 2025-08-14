package gorosheg.pulsiq.monitoring.presentation

import android.content.Context
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringEffect
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.MonitoringUiStateMapper
import gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService.PulseMonitoringService
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MonitoringViewModel(
    private val heartRateDevice: HeartRateDevice,
    private val thresholdsRepository: ThresholdsRepository,
) : BaseViewModel<MonitoringState, MonitoringUiState, MonitoringEffect>(MonitoringState(), MonitoringUiStateMapper()) {

    init {
        subscribeToPulse()
        subscribeToThresholds()
    }

    fun startMonitoring(context: Context) {
        state { copy(isTracking = true) }
        heartRateDevice.startScan()
        PulseMonitoringService.start(context)
    }

    fun stopMonitoring(context: Context) {
        state { copy(isTracking = false, pulse = 0) }
        heartRateDevice.disconnect()
        PulseMonitoringService.stop(context)
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
            .onEach { lower -> state { copy(lowerThreshold = lower) } }
            .launchIn(viewModelScope)

        thresholdsRepository.upperThresholdFlow
            .onEach { upper -> state { copy(upperThreshold = upper) } }
            .launchIn(viewModelScope)
    }
}
