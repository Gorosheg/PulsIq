package gorosheg.pulsiq

import com.example.storage.storageModule
import gorosheg.pulsiq.bluetooth.di.bluetoothModule
import gorosheg.pulsiq.common.di.commonModule
import gorosheg.pulsiq.common.activity_running_checker.HeartBeatTrackerLauncher
import gorosheg.pulsiq.monitoring.di.monitoringModule
import gorosheg.pulsiq.pulse_notification.di.pulseNotificationModule
import gorosheg.pulsiq.settings.di.settingsModule
import gorosheg.pulsiq.statistics.di.statisticsModule
import gorosheg.pulsiq.ui.di.commonUiModule
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules: List<Module>
    get() {
        return listOf(
            appModule,
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

private val appModule = module {
    single<HeartBeatTrackerLauncher> { HeartBeatTrackerLauncherImpl(get()) }
}