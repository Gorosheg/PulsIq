package gorosheg.pulsiq.device_connection.ui.component.device_list

import gorosheg.pulsiq.device_connection.R

internal data class UiBluetoothDevice(
    val name: String,
    val address: String,
    val connectingState: ConnectingState
) {
    internal enum class ConnectingState(internal val buttonText: Int) {
        NOT_CONNECTED(R.string.connect),
        CONNECTING(R.string.connecting),
        CONNECTED(R.string.disconect)
    }
}