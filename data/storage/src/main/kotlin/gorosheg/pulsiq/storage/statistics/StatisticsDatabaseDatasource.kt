package gorosheg.pulsiq.storage.statistics

import gorosheg.pulsiq.common.model.PulseStatistic
import kotlinx.coroutines.flow.Flow

interface StatisticsDatabaseDatasource {

    suspend fun createStatisticsSession()

    suspend fun stopStatisticsSession()

    suspend fun addPulse(pulse: Int)

    fun getPulse(): Flow<List<PulseStatistic>>

    suspend fun getPulse(id: Int): PulseStatistic?

    suspend fun deletePulseStatistic(id: Int)

    suspend fun clearAll()
}