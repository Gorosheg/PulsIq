package gorosheg.pulsiq.ui.di

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
            thresholdsRepository = get<ThresholdsRepository>()
        )
    }
}