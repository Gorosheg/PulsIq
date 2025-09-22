package gorosheg.pulsiq.device_connection.presentation.model

import gorosheg.pulsiq.bluetooth.model.DomainBluetoothDevice

internal data class DeviceConnectionState(
    val isScanning: Boolean = false,
    val devices: List<DomainBluetoothDevice> = emptyList(),
    val connectedAddress: String? = null,
    val connectingAddress: String? = null,
    val error: ErrorType? = null
)

internal enum class ErrorType {
    NO_CONNECTING,
}