package gorosheg.pulsiq.bluetooth

import kotlinx.coroutines.flow.Flow

interface HeartBeatDataSource {

    fun subscribeHeartRateFlow(): Flow<Int>

    fun startScan()

    fun disconnect()
}