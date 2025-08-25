package gorosheg.pulsiq

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import gorosheg.pulsiq.common.navigation.AppEnabledProvider

internal class AppEnabledProviderImpl : AppEnabledProvider, DefaultLifecycleObserver {

    override var isAppEnabled: Boolean = false
        private set

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    override fun onStart(owner: LifecycleOwner) {
        isAppEnabled = true
    }

    override fun onStop(owner: LifecycleOwner) {
        isAppEnabled = false
    }
}