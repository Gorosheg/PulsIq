package gorosheg.pulsiq

import android.app.Application
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.utils.bluetoothPermissionsGranted
import gorosheg.pulsiq.pulse_alert.PulseAlertRepository
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import kotlin.getValue

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(appModules)
        }

        val pulseAlertRepository: PulseAlertRepository by inject()
        val heartBeatDataSource: HeartBeatDataSource by inject()

        pulseAlertRepository.initialize()
        
        if (bluetoothPermissionsGranted) {
            heartBeatDataSource.startScan()
        }
    }
}