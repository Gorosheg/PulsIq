package gorosheg.pulsiq

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import gorosheg.pulsiq.common.activityRunningChecker.ActivityRunningChecker

internal class ActivityRunningCheckerImpl : ActivityRunningChecker, DefaultLifecycleObserver {

    override var isActivityRunning: Boolean = true
        private set

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        isActivityRunning = true
    }

    override fun onDestroy(owner: LifecycleOwner) {
        isActivityRunning = false
        super.onDestroy(owner)
    }
}