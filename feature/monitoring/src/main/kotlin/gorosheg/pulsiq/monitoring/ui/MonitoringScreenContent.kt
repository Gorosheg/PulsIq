package gorosheg.pulsiq.monitoring.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal fun MonitoringScreenContent(
    multiplePermissionState: MultiplePermissionsState,
    bpm: Int,
    startTracking: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Мониторинг пульса", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(24.dp))
        if (multiplePermissionState.allPermissionsGranted) {
            Text("Текущий пульс: $bpm bpm", fontSize = 32.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = startTracking::invoke) {
                Text("Подключиться к пульсометру")
            }
        } else {
            Text("Необходимо предоставить разрешения на Bluetooth и геолокацию.")
        }
    }
}