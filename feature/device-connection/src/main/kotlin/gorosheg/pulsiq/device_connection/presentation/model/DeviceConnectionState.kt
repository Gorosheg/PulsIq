package gorosheg.pulsiq.device_connection.presentation.model

import gorosheg.pulsiq.device_connection.ui.model.UiBleDevice

internal data class DeviceConnectionState(
	val isScanning: Boolean = false,
	val devices: List<UiBleDevice> = emptyList(),
	val connectedAddress: String? = null,
	val connectingAddress: String? = null,
	val error: String? = null
)