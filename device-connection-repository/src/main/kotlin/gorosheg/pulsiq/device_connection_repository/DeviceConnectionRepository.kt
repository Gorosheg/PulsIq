package gorosheg.pulsiq.device_connection_repository

import gorosheg.pulsiq.bluetooth.model.BleDevice
import kotlinx.coroutines.flow.StateFlow

interface DeviceConnectionRepository {
    val devices: StateFlow<List<BleDevice>>

    fun startScan()
    fun stopScan()

    suspend fun connect(address: String): Result<Unit>
    fun disconnect()
}