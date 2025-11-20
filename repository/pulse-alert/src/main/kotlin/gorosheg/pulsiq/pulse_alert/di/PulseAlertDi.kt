package gorosheg.pulsiq.pulse_alert.di

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.pulse_alert.PulseAlertRepository
import gorosheg.pulsiq.pulse_alert.PulseAlertRepositoryImpl
import gorosheg.pulsiq.storage.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val pulseAlertModule = module {
    factory {
        val context = androidContext()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            context.getSystemService(VibratorManager::class.java)?.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
        }
    }

    single<PulseAlertRepository> {
        PulseAlertRepositoryImpl(
            bluetoothRepository = get<BluetoothRepository>(),
            settingsRepository = get<SettingsRepository>(),
            vibrator = get()
        )
    }
}