package gorosheg.pulsiq.bluetooth

import gorosheg.pulsiq.bluetooth.model.DomainBluetoothDevice
import kotlinx.coroutines.flow.Flow

interface BluetoothRepository {

    val availableDevices: Flow<List<DomainBluetoothDevice>>

    val heartRateFlow: Flow<Int>

    val connectedDevice: Flow<String?>

    fun startScan()

    fun stopScan()

    suspend fun connect(address: String)

    fun disconnect()
}