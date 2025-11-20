package gorosheg.pulsiq.device_connection.ui.component.device_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.device_connection.R
import gorosheg.pulsiq.ui.BlueButton
import gorosheg.pulsiq.ui.MyAppTheme


@Composable
internal fun ColumnScope.DeviceList(
    devices: List<UiBluetoothDevice>,
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
        itemsIndexed(devices, key = { _, d -> d.address }) { index, d ->
            DeviceRow(
                device = d,
                onClick = { onDeviceClick(d.address) },
                onDisconnect = onDisconnect
            )
            if (index < devices.lastIndex) {
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
            text = device.name.ifBlank { stringResource(R.string.unnamed_device) },
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        when (device.connectingState) {
            UiBluetoothDevice.ConnectingState.CONNECTED -> {
                OutlinedButton(
                    onClick = onDisconnect,
                    content = { Text(stringResource(device.connectingState.buttonText)) }
                )
            }

            UiBluetoothDevice.ConnectingState.CONNECTING -> {
                AssistChip(
                    onClick = {},
                    label = { Text(stringResource(device.connectingState.buttonText)) }
                )
            }

            UiBluetoothDevice.ConnectingState.NOT_CONNECTED -> {
                BlueButton(
                    text = device.connectingState.buttonText,
                    enabled = true,
                    onClick = onClick,
                )
            }
        }
    }
}

@Preview
@Composable
private fun DeviceListPreview() {
    MyAppTheme {
        val items = listOf(
            UiBluetoothDevice(
                name = "Polar H10",
                address = "00:11:22:33:44:55",
                connectingState = UiBluetoothDevice.ConnectingState.NOT_CONNECTED
            ),
            UiBluetoothDevice(
                name = "Mi Band 7",
                address = "AA:BB:CC:DD:EE:FF",
                connectingState = UiBluetoothDevice.ConnectingState.CONNECTING
            ),
            UiBluetoothDevice(
                name = "Garmin HRM",
                address = "12:34:56:78:90:AB",
                connectingState = UiBluetoothDevice.ConnectingState.CONNECTED
            )
        )
        Column(modifier = Modifier.fillMaxWidth()) {
            DeviceList(
                devices = items,
                onDeviceClick = {},
                onDisconnect = {}
            )
        }
    }
}