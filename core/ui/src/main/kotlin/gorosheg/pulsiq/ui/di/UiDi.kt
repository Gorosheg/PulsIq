package gorosheg.pulsiq.ui.di

import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import gorosheg.pulsiq.ui.alert.PulseAlertRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val commonUiModule = module {
    single {
        PulseAlertRepository(
            context = androidContext(),
            heartBeatDataSource = get<HeartBeatDataSource>(),
            thresholdsRepository = get<ThresholdsRepository>()
        )
    }
}