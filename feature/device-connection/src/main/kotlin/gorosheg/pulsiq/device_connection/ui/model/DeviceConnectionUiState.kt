package gorosheg.pulsiq.device_connection.ui.model

internal class DeviceConnectionUiState(
    val isScanning: Boolean = false,
    val devices: List<UiBleDevice> = emptyList(),
    val connectedAddress: String? = null,
    val connectingAddress: String? = null,
    val error: String? = null
)
data class UiBleDevice(
    val name: String,
    val address: String,
)