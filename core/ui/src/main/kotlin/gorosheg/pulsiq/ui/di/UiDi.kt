package gorosheg.pulsiq.ui.di

import android.content.Context
import android.os.Build
import android.os.Vibrator
import android.os.VibratorManager
import com.example.storage.ThresholdsRepository
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.ui.alert.PulseAlertRepository
import gorosheg.pulsiq.ui.alert.PulseAlertRepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonUiModule = module {
    single<PulseAlertRepository> {
        PulseAlertRepositoryImpl(
            context = androidContext(),
            heartBeatDataSource = get<HeartBeatDataSource>(),
            thresholdsRepository = get<ThresholdsRepository>(),
            vibrator = get<Vibrator>()
        )
    }

    single<Vibrator> {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            androidContext().getSystemService(VibratorManager::class.java).defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            androidContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}