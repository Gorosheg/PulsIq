package gorosheg.pulsiq

import android.app.Application
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.ui.alert.PulseAlertRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import kotlin.getValue

class App : Application() {

    private val pulseAlertRepository: PulseAlertRepository by inject()
    private val heartBeatDataSource: HeartBeatDataSource by inject()

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModules)
        }

        pulseAlertRepository.initialize()
        heartBeatDataSource.startScan()
    }
}