package gorosheg.pulsiq.device_connection_repository

import gorosheg.pulsiq.bluetooth.HeartBeatDataSource
import gorosheg.pulsiq.bluetooth.model.BleDevice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext

class DeviceConnectionRepositoryImpl(
    private val heartBeatDataSource: HeartBeatDataSource,
) : DeviceConnectionRepository {

    override val devices: MutableStateFlow<List<BleDevice>> = MutableStateFlow(emptyList())

    override fun startScan() {
        heartBeatDataSource.startScan()
    }

    override fun stopScan() {
        heartBeatDataSource.stopScan()
    }

    override suspend fun connect(address: String): Result<Unit> = withContext(Dispatchers.Main.immediate) {
        heartBeatDataSource.connect(address)
    }

    override fun disconnect() {
        heartBeatDataSource.disconnect()
    }
}