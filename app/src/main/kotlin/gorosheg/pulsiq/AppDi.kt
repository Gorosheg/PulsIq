package gorosheg.pulsiq

import com.example.storage.storageModule
import gorosheg.pulsiq.common.navigation.commonModule
import gorosheg.pulsiq.monitoring.di.monitoringModule
import gorosheg.pulsiq.settings.di.settingsModule
import gorosheg.pulsiq.statistics.di.statisticsModule
import org.koin.core.module.Module

val appModules: List<Module>
    get() = listOf(
        commonModule,
        storageModule,
        monitoringModule,
        statisticsModule,
        settingsModule,
    )