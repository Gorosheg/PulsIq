package gorosheg.pulsiq.bluetooth.di

import gorosheg.pulsiq.bluetooth.BleHeartRateDevice
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val bluetoothModule = module {
    singleOf(::BleHeartRateDevice) bind HeartRateDevice::class
}