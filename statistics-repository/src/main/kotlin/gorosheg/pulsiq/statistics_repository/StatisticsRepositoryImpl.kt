package gorosheg.pulsiq.statistics_repository

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.storage.statistics.StatisticsDatabaseDatasource
import kotlinx.coroutines.flow.Flow

class StatisticsRepositoryImpl(
    private val statisticsDatabaseDatasource: StatisticsDatabaseDatasource
) : StatisticsRepository {

    override suspend fun createStatisticsSession() {
        statisticsDatabaseDatasource.createStatisticsSession()
    }

    override suspend fun stopStatisticsSession(){
        statisticsDatabaseDatasource.stopStatisticsSession()
    }

    override suspend fun addPulse(pulse: Int) {
        statisticsDatabaseDatasource.addPulse(pulse)
    }

    override fun getPulse(): Flow<List<PulseStatistic>> {
        return statisticsDatabaseDatasource.getPulse()
    }

    override suspend fun getPulse(id: Int): PulseStatistic? {
        return statisticsDatabaseDatasource.getPulse(id)
    }

    override suspend fun deletePulseStatistic(id: Int) {
        statisticsDatabaseDatasource.deletePulseStatistic(id)
    }

    override suspend fun clearAll() {
        statisticsDatabaseDatasource.clearAll()
    }
}