package gorosheg.pulsiq.bluetooth.di

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import android.os.ParcelUuid
import gorosheg.pulsiq.bluetooth.BluetoothConnector
import gorosheg.pulsiq.bluetooth.BluetoothRepository
import gorosheg.pulsiq.bluetooth.BluetoothRepositoryImpl
import gorosheg.pulsiq.bluetooth.BluetoothRepositoryImpl.Companion.heartRateServiceUUID
import gorosheg.pulsiq.bluetooth.BluetoothScanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val bluetoothModule = module {
    single<BluetoothRepository> {
        BluetoothRepositoryImpl(
            context = androidContext(),
            scope = CoroutineScope(Dispatchers.IO),
            bluetoothScanner = get(),
            bluetoothConnector = get(),
            filter = get(),
            settings = get()
        )
    }

    single <BluetoothConnector>{
        BluetoothConnector()
    }

    single <BluetoothScanner>{
        BluetoothScanner(get())
    }

    factory<BluetoothLeScanner?> {
        val bluetoothManager = androidContext().getSystemService(BluetoothManager::class.java)
        val bluetoothAdapter = bluetoothManager?.adapter
        bluetoothAdapter?.bluetoothLeScanner
    }

    single<ScanFilter> {
        ScanFilter.Builder()
            .setServiceUuid(ParcelUuid(heartRateServiceUUID))
            .build()
    }
    single<ScanSettings> {
        ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .setCallbackType(ScanSettings.CALLBACK_TYPE_FIRST_MATCH or ScanSettings.CALLBACK_TYPE_MATCH_LOST)
            .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
            .setReportDelay(0)
            .build()
    }
}