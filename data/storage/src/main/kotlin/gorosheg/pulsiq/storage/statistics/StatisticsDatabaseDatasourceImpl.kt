package gorosheg.pulsiq.storage.statistics

import gorosheg.pulsiq.storage.dao.StatisticsDao
import gorosheg.pulsiq.storage.mapper.toDomain
import gorosheg.pulsiq.storage.mapper.toEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class StatisticsDatabaseDatasourceImpl(
    private val statisticsDao: StatisticsDao,
) : StatisticsDatabaseDatasource {

    override fun getPulse(): Flow<List<Int>> {
        return statisticsDao.getPulseList()
            .map { list -> list.toDomain() }
            .catch { emit(emptyList()) }
            .flowOn(Dispatchers.IO)
    }

    override suspend fun addPulse(pulse: Int) {
        withContext(Dispatchers.IO) {
            statisticsDao.addPulse(pulse.toEntity())
        }
    }

    override suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            statisticsDao.clearAll()
        }
    }
}