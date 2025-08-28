package gorosheg.pulsiq.bluetooth.di

import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.bluetooth.HeartBeatDataSourceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothModule = module {
    singleOf(::HeartBeatDataSourceImpl) bind HeartBeatDataSource::class
}