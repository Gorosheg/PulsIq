package gorosheg.pulsiq.statistics_repository

import gorosheg.pulsiq.storage.statistics.StatisticsDatabaseDatasource

class StatisticsRepositoryImpl(
    private val statisticsDatabaseDatasource: StatisticsDatabaseDatasource
) : StatisticsRepository {

    override suspend fun addPulse(pulse: Int) {
        statisticsDatabaseDatasource.addPulse(pulse)
    }
}