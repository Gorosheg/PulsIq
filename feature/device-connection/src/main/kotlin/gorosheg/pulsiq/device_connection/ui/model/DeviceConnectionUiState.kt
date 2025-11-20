package gorosheg.pulsiq.device_connection.ui.model

import gorosheg.pulsiq.device_connection.R
import gorosheg.pulsiq.device_connection.ui.component.device_list.UiBluetoothDevice

internal class DeviceConnectionUiState(
    val devices: List<UiBluetoothDevice> = emptyList(),
    val error: Int? = null,
    val scanningButtonsState: ScanningButtonsState = ScanningButtonsState()
)

internal class ScanningButtonsState(
    val isScanning: Boolean = false,
    val scanningButtonText: Int = R.string.scan,
    val stopButtonText: Int = R.string.stop,
)