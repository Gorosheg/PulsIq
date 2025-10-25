package gorosheg.pulsiq.statistics_repository

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.storage.statistics.StatisticsDatabaseDatasource
import kotlinx.coroutines.flow.Flow

class StatisticsRepositoryImpl(
    private val statisticsDatabaseDatasource: StatisticsDatabaseDatasource
) : StatisticsRepository {

    override suspend fun addPulse(pulse: Int) {
        statisticsDatabaseDatasource.addPulse(pulse)
    }

    override suspend fun getPulse(): Flow<List<PulseStatistic>> {
        return statisticsDatabaseDatasource.getPulse()
    }
}