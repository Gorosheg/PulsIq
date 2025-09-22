package gorosheg.pulsiq.bluetooth.model

import android.bluetooth.BluetoothDevice

data class DomainBluetoothDevice(
    val rssi: Int,
    val name: String,
    val device: BluetoothDevice,
    val isConnectable: Boolean
)