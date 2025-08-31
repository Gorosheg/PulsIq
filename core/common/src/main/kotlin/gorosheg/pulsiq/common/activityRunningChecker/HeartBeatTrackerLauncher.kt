package gorosheg.pulsiq.common.activityRunningChecker

interface HeartBeatTrackerLauncher {

    fun changeActivityState(isRunning: Boolean)
    fun changeServiceState(isRunning: Boolean)
}