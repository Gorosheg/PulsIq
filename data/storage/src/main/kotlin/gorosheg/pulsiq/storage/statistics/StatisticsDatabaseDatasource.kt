package gorosheg.pulsiq.storage.statistics

import kotlinx.coroutines.flow.Flow

interface StatisticsDatabaseDatasource {

    fun getPulse(): Flow<List<Int>>

    suspend fun addPulse(pulse: Int)

    suspend fun clearAll()
}