package gorosheg.pulsiq.statistics

import gorosheg.pulsiq.common.model.PulseStatistic
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    suspend fun createStatisticsSession(name: String)

    suspend fun stopStatisticsSession()

    suspend fun changeStatisticsSessionName(id: Int? = null, name: String)

    suspend fun addPulse(pulse: Int)

    fun getPulse(): Flow<List<PulseStatistic>>

    suspend fun getPulse(id: Int): PulseStatistic?

    suspend fun deletePulseStatistic(id: Int)

    suspend fun clearAll()
}