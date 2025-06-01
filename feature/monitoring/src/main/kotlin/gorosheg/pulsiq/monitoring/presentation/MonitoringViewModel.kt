package gorosheg.pulsiq.monitoring.presentation

import androidx.lifecycle.ViewModel
import gorosheg.pulsiq.bluetooth.HeartRateDevice

class MonitoringViewModel(private val heartRateDevice: HeartRateDevice) : ViewModel() {

    val heartRate = heartRateDevice.heartRateFlow

    fun startMonitoring() = heartRateDevice.startScan()

    override fun onCleared() {
        heartRateDevice.disconnect()
        super.onCleared()
    }
}
