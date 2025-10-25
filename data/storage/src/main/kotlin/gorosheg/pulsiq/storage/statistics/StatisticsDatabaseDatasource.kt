package gorosheg.pulsiq.storage.statistics

import gorosheg.pulsiq.common.model.PulseStatistic
import kotlinx.coroutines.flow.Flow

interface StatisticsDatabaseDatasource {

    fun getPulse(): Flow<List<PulseStatistic>>

    suspend fun addPulse(pulse: Int)

    suspend fun clearAll()

    suspend fun createStatisticsSession()

    suspend fun getPulse(id: Int): PulseStatistic?

    suspend fun deletePulseStatistic(id: Int)
}