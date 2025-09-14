package gorosheg.pulsiq.bluetooth.model

data class BleDevice(
    val name: String,
    val address: String,
    val rssi: Int,
    val isConnectable: Boolean
)