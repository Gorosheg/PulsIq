package gorosheg.pulsiq.bluetooth

import gorosheg.pulsiq.bluetooth.model.BleDevice
import kotlinx.coroutines.flow.Flow

interface HeartBeatDataSource {

    fun subscribeHeartRateFlow(): Flow<Int>

    fun subscribeAvailableDevicesFlow(): Flow<List<BleDevice>>

    fun startScan()

    fun stopScan()

    fun connect(address: String, connected: (Boolean) -> Unit)

    fun disconnect()
}