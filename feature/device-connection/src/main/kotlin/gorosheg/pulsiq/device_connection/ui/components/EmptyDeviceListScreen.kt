package gorosheg.pulsiq.device_connection.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import gorosheg.pulsiq.device_connection.R
import gorosheg.pulsiq.ui.MyAppTheme

@Composable
internal fun EmptyDeviceListScreen() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.noAvailableDevices),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview
@Composable
private fun EmptyDeviceListScreenPreview() {
    MyAppTheme {
        EmptyDeviceListScreen()
    }
}