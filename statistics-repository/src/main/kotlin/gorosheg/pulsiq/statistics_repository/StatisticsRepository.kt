package gorosheg.pulsiq.statistics_repository

import kotlinx.coroutines.flow.Flow

interface StatisticsRepository {

    suspend fun addPulse(pulse: Int)

    suspend fun getPulse(): Flow<List<Int>>
}