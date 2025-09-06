package gorosheg.pulsiq.statistics_repository

interface StatisticsRepository {

    suspend fun addPulse(pulse: Int)
}