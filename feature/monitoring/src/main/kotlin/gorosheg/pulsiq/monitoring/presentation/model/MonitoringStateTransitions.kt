package gorosheg.pulsiq.monitoring.presentation.model

internal fun MonitoringState.requestStopConfirmation(): MonitoringState {
    return copy(isSetNameDialogShow = true)
}

internal fun MonitoringState.cancelStopConfirmation(): MonitoringState {
    return copy(isSetNameDialogShow = false, sessionName = "")
}

internal fun MonitoringState.confirmStop(): MonitoringState {
    return copy(
        isTracking = false,
        pulse = 0,
        isSetNameDialogShow = false,
        sessionName = ""
    )
}
