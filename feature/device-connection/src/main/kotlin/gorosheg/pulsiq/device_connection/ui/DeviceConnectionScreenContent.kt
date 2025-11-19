package gorosheg.pulsiq.device_connection.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.device_connection.ui.components.DeviceList
import gorosheg.pulsiq.device_connection.ui.components.EmptyDeviceListScreen
import gorosheg.pulsiq.device_connection.ui.components.ScanningButtons
import gorosheg.pulsiq.device_connection.ui.model.ConnectingState
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.UiBluetoothDevice
import gorosheg.pulsiq.ui.MyAppTheme

@Composable
internal fun DeviceConnectionScreenContent(
    state: DeviceConnectionUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onDeviceClick: (String) -> Unit,
    onDisconnect: () -> Unit
) {
    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            ScanningButtons(state, onStartScan, onStopScan)
            Spacer(Modifier.height(16.dp))

            if (state.devices.isEmpty()) {
                EmptyDeviceListScreen()
            } else {
                DeviceList(
                    devices = state.devices,
                    onDeviceClick = onDeviceClick,
                    onDisconnect = onDisconnect
                )
            }

            if (state.isScanning) {
                Scanning()
            }

            state.error?.let { errorText ->
                Error(errorText)
            }
        }
    }
}


@Composable
private fun Scanning() {
    Spacer(Modifier.height(12.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun Error(errorText: Int) {
    Spacer(Modifier.height(8.dp))
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(errorText),
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Preview()
@Composable
private fun DeviceConnectionScreenContentPreview() {
    MyAppTheme {
        DeviceConnectionScreenContent(
            state = DeviceConnectionUiState(
                isScanning = false,
                devices = emptyList(),
                error = null,
            ),
            onStartScan = {},
            onStopScan = {},
            onDeviceClick = {},
            onDisconnect = {}
        )
    }
}

@Preview()
@Composable
private fun DeviceConnectionScreenContentScanningPreview() {
    MyAppTheme {
        DeviceConnectionScreenContent(
            state = DeviceConnectionUiState(
                isScanning = true,
                devices = listOf(
                    UiBluetoothDevice(
                        name = "Heart Rate Monitor",
                        address = "00:11:22:33:44:55",
                        connectingState = ConnectingState.NOT_CONNECTED
                    ),
                    UiBluetoothDevice(
                        name = "Fitness Tracker",
                        address = "AA:BB:CC:DD:EE:FF",
                        connectingState = ConnectingState.CONNECTING
                    )
                ),
                error = null,
            ),
            onStartScan = {},
            onStopScan = {},
            onDeviceClick = {},
            onDisconnect = {}
        )
    }
}

@Preview()
@Composable
private fun DeviceConnectionScreenContentConnectedPreview() {
    MyAppTheme {
        DeviceConnectionScreenContent(
            state = DeviceConnectionUiState(
                isScanning = false,
                devices = listOf(
                    UiBluetoothDevice(
                        name = "Heart Rate Monitor",
                        address = "00:11:22:33:44:55",
                        connectingState = ConnectingState.CONNECTED
                    ),
                    UiBluetoothDevice(
                        name = "Fitness Tracker",
                        address = "AA:BB:CC:DD:EE:FF",
                        connectingState = ConnectingState.NOT_CONNECTED
                    ),
                    UiBluetoothDevice(
                        name = "Smart Watch",
                        address = "11:22:33:44:55:66",
                        connectingState = ConnectingState.CONNECTING
                    )
                ),
                error = null,
            ),
            onStartScan = {},
            onStopScan = {},
            onDeviceClick = {},
            onDisconnect = {}
        )
    }
}