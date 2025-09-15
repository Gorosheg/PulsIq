package gorosheg.pulsiq.device_connection.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import gorosheg.pulsiq.device_connection.ui.model.DeviceConnectionUiState
import gorosheg.pulsiq.device_connection.ui.model.UiBleDevice

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DeviceConnectionScreenContent(
    state: DeviceConnectionUiState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit,
    onDeviceClick: (String) -> Unit,
    onDisconnect: () -> Unit
) {
    val context = LocalContext.current
    val needsScanPermission = remember { mutableStateOf(!hasBleScanPermission(context)) }
    val needsConnectPermission = remember { mutableStateOf(!hasBleConnectPermission(context)) }
    val needsLocationPermission = remember { mutableStateOf(!hasLocationPermissionIfNeeded(context)) }

    val permissionsToRequest = remember {
        buildList {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (needsScanPermission.value) add(Manifest.permission.BLUETOOTH_SCAN)
                if (needsConnectPermission.value) add(Manifest.permission.BLUETOOTH_CONNECT)
            } else {
                if (needsLocationPermission.value) add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }.toTypedArray()
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = {
            needsScanPermission.value = !hasBleScanPermission(context)
            needsConnectPermission.value = !hasBleConnectPermission(context)
            needsLocationPermission.value = !hasLocationPermissionIfNeeded(context)
        }
    )

    LaunchedEffect(Unit) {
        if (permissionsToRequest.isNotEmpty()) {
            launcher.launch(permissionsToRequest)
        }
    }

    Scaffold { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onStartScan,
                    enabled = !state.isScanning && hasAllNeededPermissions(context),
                ) { Text("Сканировать") }

                Spacer(Modifier.width(12.dp))

                OutlinedButton(
                    onClick = onStopScan,
                    enabled = state.isScanning
                ) { Text("Стоп") }

                Spacer(Modifier.weight(1f))
            }

            Spacer(Modifier.height(16.dp))

            if (!hasAllNeededPermissions(context)) {
                Text(
                    "Нужно выдать разрешения на Bluetooth${if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) " и геолокацию" else ""}.",
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(8.dp))
            }

            if (state.devices.isEmpty()) {
                Text(
                    text = "Нет доступных устройств",
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
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
                            isConnected = state.connectedAddress == d.address,
                            isConnecting = state.connectingAddress == d.address,
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

            if (state.isScanning) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            state.error?.let { err ->
                Spacer(Modifier.height(8.dp))
                Text(err, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}

@Composable
private fun DeviceRow(
    device: UiBleDevice,
    isConnected: Boolean,
    isConnecting: Boolean,
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
            text = device.name.ifBlank { "Безымянное устройство" },
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(8.dp))

        when {
            isConnected -> OutlinedButton(onClick = onDisconnect) {
                Text("Отключить")
            }

            isConnecting -> AssistChip(onClick = {}, label = { Text("Подключение...") })
            else -> Button(onClick = onClick) { Text("Подключить") }
        }
    }
}

private fun hasAllNeededPermissions(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        hasBleScanPermission(context) && hasBleConnectPermission(context)
    } else {
        hasLocationPermissionIfNeeded(context)
    }
}

private fun hasBleScanPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_SCAN
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}

private fun hasBleConnectPermission(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.BLUETOOTH_CONNECT
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}

private fun hasLocationPermissionIfNeeded(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    } else true
}