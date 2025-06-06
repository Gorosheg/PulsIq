package gorosheg.pulsiq.monitoring.presentation

import android.content.Context
import androidx.lifecycle.ViewModel
import gorosheg.pulsiq.bluetooth.HeartRateDevice
import gorosheg.pulsiq.monitoring.ui.heartRateMonitoringService.PulseMonitoringService

class MonitoringViewModel(private val heartRateDevice: HeartRateDevice ) : ViewModel() {

    val heartRate = heartRateDevice.heartRateFlow

    fun startMonitoring(context: Context) {
        heartRateDevice.startScan()
        PulseMonitoringService.start(context = context)
    }

    override fun onCleared() {
        heartRateDevice.disconnect()
        super.onCleared()
    }
}
