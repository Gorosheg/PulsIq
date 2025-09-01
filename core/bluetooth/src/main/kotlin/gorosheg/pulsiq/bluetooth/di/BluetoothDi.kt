package gorosheg.pulsiq.bluetooth.di

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.bluetooth.HeartBeatDataSourceImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val bluetoothModule = module {
    single<HeartBeatDataSource> {
        HeartBeatDataSourceImpl(
            context = androidContext(),
            scanner = get(),
            scope = CoroutineScope(Dispatchers.IO)
        )
    }

    single<BluetoothLeScanner?> {
        val bluetoothManager = androidContext().getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager?.adapter
        bluetoothAdapter?.bluetoothLeScanner
    }
}