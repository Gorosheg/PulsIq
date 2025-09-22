package gorosheg.pulsiq

import android.app.Application
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.common.utils.hasAllRequiredPermissions
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
        val bluetoothRepository: BluetoothRepository by inject()

        pulseAlertRepository.initialize()

        if (hasAllRequiredPermissions()) {
            bluetoothRepository.startScan()
        }
    }
}