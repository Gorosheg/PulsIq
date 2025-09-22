package gorosheg.pulsiq.monitoring.ui.model

import androidx.compose.ui.graphics.Color
import gorosheg.pulsiq.ui.Blue

internal class MonitoringUiState(
    val isTracking: Boolean = false,
    val pulse: Int = 0,
    val heartColor: Color = Blue,
    val heartRateSpeed: Int = 1000,
    val noBluetoothPermissionText: Int
)