package gorosheg.pulsiq.monitoring.ui

import android.Manifest
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cafe.adriel.voyager.core.screen.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import gorosheg.pulsiq.monitoring.presentation.MonitoringViewModel
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalPermissionsApi::class)
internal class MonitoringScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel = getViewModel<MonitoringViewModel>()
        val bpm by viewModel.heartRate.collectAsState()
        val context = LocalContext.current.applicationContext

        val permissions = remember {
            mutableListOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ).apply {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    add(Manifest.permission.BLUETOOTH_SCAN)
                    add(Manifest.permission.BLUETOOTH_CONNECT)
                } else {
                    add(Manifest.permission.BLUETOOTH)
                    add(Manifest.permission.BLUETOOTH_ADMIN)
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        val multiplePermissionState = rememberMultiplePermissionsState(permissions)

        LaunchedEffect(Unit) {
            multiplePermissionState.launchMultiplePermissionRequest()
        }

        MonitoringScreenContent(
            multiplePermissionState = multiplePermissionState,
            bpm = bpm,
            startTracking = { viewModel.startMonitoring(context) }
        )
    }
}