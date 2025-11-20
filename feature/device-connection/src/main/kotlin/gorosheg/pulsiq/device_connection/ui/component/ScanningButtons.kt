package gorosheg.pulsiq.device_connection.ui.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import gorosheg.pulsiq.device_connection.ui.model.ScanningButtonsState
import gorosheg.pulsiq.ui.BlueButton
import gorosheg.pulsiq.ui.MyAppTheme

@Composable
internal fun ScanningButtons(
    scanningButtonsState: ScanningButtonsState,
    onStartScan: () -> Unit,
    onStopScan: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        BlueButton(
            text = scanningButtonsState.scanningButtonText,
            enabled = !scanningButtonsState.isScanning,
            onClick = onStartScan
        )
        Spacer(Modifier.width(12.dp))
        OutlinedButton(
            onClick = onStopScan,
            enabled = scanningButtonsState.isScanning
        ) { Text(stringResource(scanningButtonsState.stopButtonText)) }
        Spacer(Modifier.weight(1f))
    }
}

@Preview
@Composable
private fun ScanningButtonsPreview_Off() {
    MyAppTheme {
        ScanningButtons(
            scanningButtonsState = ScanningButtonsState(isScanning = false),
            onStartScan = {},
            onStopScan = {}
        )
    }
}

@Preview
@Composable
private fun ScanningButtonsPreview_On() {
    MyAppTheme {
        ScanningButtons(
            scanningButtonsState = ScanningButtonsState(isScanning = true),
            onStartScan = {},
            onStopScan = {}
        )
    }
}