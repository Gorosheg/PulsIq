package gorosheg.pulsiq.monitoring.ui

import gorosheg.pulsiq.common.viewModel.ui_state_mapper.UiStateMapper
import gorosheg.pulsiq.monitoring.presentation.model.MonitoringState
import gorosheg.pulsiq.monitoring.ui.model.MonitoringUiState

internal class MonitoringUiStateMapper : UiStateMapper<MonitoringState, MonitoringUiState> {

    override fun MonitoringState.map(): MonitoringUiState {
        return MonitoringUiState(
            isTracking = isTracking,
            pulse = pulse,
            heartColor = HeartStateBuilder.buildHeartColor(
                pulse = pulse,
                minPulse = lowerThreshold,
                maxPulse = upperThreshold
            ),
            heartRateSpeed = HeartStateBuilder.buildHeartRateSpeed(pulse),
        )
    }
}