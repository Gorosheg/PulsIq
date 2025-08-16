package gorosheg.pulsiq

import com.example.storage.storageModule
import gorosheg.pulsiq.bluetooth.di.bluetoothModule
import gorosheg.pulsiq.common.navigation.AppEnabledProvider
import gorosheg.pulsiq.common.navigation.commonModule
import gorosheg.pulsiq.monitoring.di.monitoringModule
import gorosheg.pulsiq.pulsenotification.di.pulseNotificationModule
import gorosheg.pulsiq.settings.di.settingsModule
import gorosheg.pulsiq.statistics.di.statisticsModule
import gorosheg.pulsiq.ui.di.commonUiModule
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
            pulseNotificationModule,
            commonUiModule
        )
    }

val platformModule = module {
    single<AppEnabledProvider> { AppEnabledProviderImpl() }
}