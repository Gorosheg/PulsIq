package gorosheg.pulsiq.statistics_repository

import gorosheg.pulsiq.common.model.PulseStatistic
import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    suspend fun addPulse(pulse: Int)

    suspend fun getPulse(): Flow<List<PulseStatistic>>
}