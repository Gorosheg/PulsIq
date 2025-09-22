package gorosheg.pulsiq.device_connection.ui.model

import gorosheg.pulsiq.device_connection.R

internal class DeviceConnectionUiState(
    val isScanning: Boolean = false,
    val devices: List<UiBluetoothDevice> = emptyList(),
    val error: Int? = null,
    val scanningButtonText: Int = R.string.scan,
    val stopButtonText: Int = R.string.stop,
    val noAvailableDevicesText: Int = R.string.noAvailableDevices,
    val noBluetoothPermissionText: Int,
)

internal data class UiBluetoothDevice(
    val name: String,
    val address: String,
    val connectingState: ConnectingState
)

internal enum class ConnectingState(internal val buttonText: Int) {
    NOT_CONNECTED(R.string.connect),
    CONNECTING(R.string.connecting),
    CONNECTED(R.string.disconect)
}