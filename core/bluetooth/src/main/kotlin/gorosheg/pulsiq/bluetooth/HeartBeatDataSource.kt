package gorosheg.pulsiq.bluetooth

import kotlinx.coroutines.flow.Flow

interface HeartBeatDataSource {
    val heartRateFlow: Flow<Int>
    fun startScan()
    fun disconnect()
}