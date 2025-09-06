package gorosheg.pulsiq.statistics_repository

import com.example.storage.statistics.StatisticsDatabaseDatasource

class StatisticsRepositoryImpl(
    private val statisticsDatabaseDatasource: StatisticsDatabaseDatasource
) : StatisticsRepository {

    override suspend fun addPulse(pulse: Int) {
        statisticsDatabaseDatasource.addPulse(pulse)
    }

}