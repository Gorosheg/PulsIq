package gorosheg.pulsiq.storage.statistics

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.storage.dao.StatisticsDao
import gorosheg.pulsiq.storage.mapper.toDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class StatisticsDatabaseDatasourceImpl(
    private val statisticsDao: StatisticsDao,
) : StatisticsDatabaseDatasource {

    override suspend fun createStatisticsSession() {
        withContext(Dispatchers.IO) {
            statisticsDao.createStatisticsSession()
        }
    }

    override suspend fun addPulse(pulse: Int) {
        withContext(Dispatchers.IO) {
            statisticsDao.addPulseToCurrent(pulse)
        }
    }

    override fun getPulse(): Flow<List<PulseStatistic>> {
        return statisticsDao.getPulseList()
            .map { list -> list.toDomain() }
            .catch { emit(emptyList()) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun getPulse(id: Int): PulseStatistic? {
        return withContext(Dispatchers.IO) {
            statisticsDao.getById(id)?.toDomain()
        }
    }

    override suspend fun deletePulseStatistic(id: Int) {
        withContext(Dispatchers.IO) {
            statisticsDao.deleteById(id)
        }
    }

    override suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            statisticsDao.clearAll()
        }
    }
}