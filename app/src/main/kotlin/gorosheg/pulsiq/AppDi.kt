package gorosheg.pulsiq

import com.example.storage.storageModule
import gorosheg.pulsiq.bluetooth.di.bluetoothModule
import gorosheg.pulsiq.common.navigation.commonModule
import gorosheg.pulsiq.monitoring.di.monitoringModule
import gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService.AppEnabledProvider
import gorosheg.pulsiq.settings.di.settingsModule
import gorosheg.pulsiq.statistics.di.statisticsModule
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules: List<Module>
    get() {
        return listOf(
            platformModule,
            commonModule,
            bluetoothModule,
            storageModule,
            monitoringModule,
            statisticsModule,
            settingsModule,
        )
    }

val platformModule = module {
    single<AppEnabledProvider> { AppEnabledProviderImpl() }
}