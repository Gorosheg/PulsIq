package gorosheg.pulsiq.monitoring.presentation

import android.app.Application
import androidx.lifecycle.viewModelScope
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringEffect
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import gorosheg.pulsiq.common.viewModel.BaseViewModel
import gorosheg.pulsiq.monitoring.ui.MonitoringUiStateMapper
import gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService.PulseMonitoringService
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MonitoringViewModel(
    application: Application,
    uiStateMapper: MonitoringUiStateMapper,
    private val heartRateDevice: HeartRateDevice,
) : BaseViewModel<MonitoringState, MonitoringUiState, MonitoringEffect>(application, MonitoringState(), uiStateMapper) {

    init {
        subscribeToPulse()
    }

    override fun onCleared() {
        stopMonitoring()
        super.onCleared()
    }

    fun startMonitoring() {
        state { copy(isTracking = true) }
        heartRateDevice.startScan()
        PulseMonitoringService.start(context = getApplication())
    }

    fun stopMonitoring() {
        state { copy(isTracking = false) }
        heartRateDevice.disconnect()
        PulseMonitoringService.stop(context = getApplication())
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
}
