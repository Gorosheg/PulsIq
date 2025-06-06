package gorosheg.pulsiq.monitoring.presentation.model

internal data class MonitoringState(
	val isTracking: Boolean = false,
	val pulse: Int = 0,
)