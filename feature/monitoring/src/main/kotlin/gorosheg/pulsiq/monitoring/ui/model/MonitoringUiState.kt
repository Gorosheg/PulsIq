package gorosheg.pulsiq.monitoring.ui.model

internal data class MonitoringUiState(
    val isTracking: Boolean = false,
    val pulse: Int = 0
)