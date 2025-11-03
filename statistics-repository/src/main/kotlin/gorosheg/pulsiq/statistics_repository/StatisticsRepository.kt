package gorosheg.pulsiq.statistics_repository

import gorosheg.pulsiq.common.model.PulseStatistic
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    suspend fun createStatisticsSession()

    suspend fun stopStatisticsSession()

    suspend fun changeStatisticsSessionName(id: Int, name: String)

    suspend fun addPulse(pulse: Int)

    fun getPulse(): Flow<List<PulseStatistic>>

    suspend fun getPulse(id: Int): PulseStatistic?

    suspend fun deletePulseStatistic(id: Int)

    suspend fun clearAll()
}