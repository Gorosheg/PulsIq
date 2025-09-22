package gorosheg.pulsiq.device_connection.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.device_connection.ui.model.ConnectingState
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.UiBluetoothDevice

@OptIn(ExperimentalMaterial3Api::class)
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
            ScanningButtons(onStartScan, state, onStopScan)
            Spacer(Modifier.height(16.dp))

            if (state.devices.isEmpty()) {
                EmptyDeviceListScreen(state)
            } else {
                DeviceList(state, onDeviceClick, onDisconnect)
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
private fun ScanningButtons(
    onStartScan: () -> Unit,
    state: DeviceConnectionUiState,
    onStopScan: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = onStartScan,
            enabled = !state.isScanning,
        ) { Text(stringResource(state.scanningButtonText)) }

        Spacer(Modifier.width(12.dp))

        OutlinedButton(
            onClick = onStopScan,
            enabled = state.isScanning
        ) { Text(stringResource(state.stopButtonText)) }

        Spacer(Modifier.weight(1f))
    }
}

@Composable
private fun EmptyDeviceListScreen(state: DeviceConnectionUiState) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(state.noAvailableDevicesText),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun ColumnScope.DeviceList(
    state: DeviceConnectionUiState,
    onDeviceClick: (String) -> Unit,
    onDisconnect: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f, fill = false)
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        itemsIndexed(state.devices, key = { _, d -> d.address }) { index, d ->
            DeviceRow(
                device = d,
                onClick = { onDeviceClick(d.address) },
                onDisconnect = onDisconnect
            )
            if (index < state.devices.lastIndex) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 4.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant
                )
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
        Text(stringResource(errorText), color = MaterialTheme.colorScheme.error)
    }
}

@Composable
private fun DeviceRow(
    device: UiBluetoothDevice,
    onClick: () -> Unit,
    onDisconnect: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = device.name,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        when (device.connectingState) {
            ConnectingState.CONNECTED -> {
                OutlinedButton(
                    onClick = onDisconnect,
                    content = { Text(stringResource(device.connectingState.buttonText)) }
                )
            }

            ConnectingState.CONNECTING -> {
                AssistChip(
                    onClick = {},
                    label = { Text(stringResource(device.connectingState.buttonText)) }
                )
            }

            ConnectingState.NOT_CONNECTED -> {
                Button(
                    onClick = onClick,
                    content = { Text(stringResource(device.connectingState.buttonText)) }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DeviceConnectionScreenContentPreview() {
    DeviceConnectionScreenContent(
        state = DeviceConnectionUiState(
            isScanning = false,
            devices = emptyList(),
            error = null,
            noBluetoothPermissionText = gorosheg.pulsiq.device_connection.R.string.noBluetoothPermission
        ),
        onStartScan = {},
        onStopScan = {},
        onDeviceClick = {},
        onDisconnect = {}
    )
}

@Preview(showBackground = true, name = "Scanning State")
@Composable
private fun DeviceConnectionScreenContentScanningPreview() {
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
            noBluetoothPermissionText = gorosheg.pulsiq.device_connection.R.string.noBluetoothPermission
        ),
        onStartScan = {},
        onStopScan = {},
        onDeviceClick = {},
        onDisconnect = {}
    )
}

@Preview(showBackground = true, name = "Connected Device")
@Composable
private fun DeviceConnectionScreenContentConnectedPreview() {
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
            noBluetoothPermissionText = gorosheg.pulsiq.device_connection.R.string.noBluetoothPermission
        ),
        onStartScan = {},
        onStopScan = {},
        onDeviceClick = {},
        onDisconnect = {}
    )
}