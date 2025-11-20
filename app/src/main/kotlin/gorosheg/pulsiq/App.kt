package gorosheg.pulsiq

import android.app.Application
import gorosheg.pulsiq.pulse_alert.PulseAlertRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModules)
        }

        val pulseAlertRepository: PulseAlertRepository by inject()

        pulseAlertRepository.initialize()
    }
}