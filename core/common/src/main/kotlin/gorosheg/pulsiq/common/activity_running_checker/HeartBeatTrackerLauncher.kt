package gorosheg.pulsiq.common.activity_running_checker

interface HeartBeatTrackerLauncher {

    fun changeActivityState(isRunning: Boolean)

    fun changeServiceState(isRunning: Boolean)
}