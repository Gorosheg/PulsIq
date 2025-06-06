package gorosheg.pulsiq.bluetooth

import kotlinx.coroutines.flow.Flow

interface HeartRateDevice {
    val heartRateFlow: Flow<Int>
    fun startScan()
    fun disconnect()
}