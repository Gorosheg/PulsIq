package gorosheg.pulsiq.bluetooth

import kotlinx.coroutines.flow.StateFlow

interface HeartRateDevice {
    val heartRateFlow: StateFlow<Int>
    fun startScan()
    fun disconnect()
}