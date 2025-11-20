package gorosheg.pulsiq.storage.statistics

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.storage.dao.StatisticsDao
import gorosheg.pulsiq.storage.mapper.toDomain
import gorosheg.pulsiq.storage.midel.StatisticsEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

internal class StatisticsDatabaseDatasourceImpl(
    private val statisticsDao: StatisticsDao,
) : StatisticsDatabaseDatasource {

    override suspend fun createStatisticsSession(name: String) {
        withContext(Dispatchers.IO) {
            statisticsDao.insert(
                StatisticsEntity(
                    dateStart = LocalDateTime.now(),
                    name = name
                )
            )
        }
    }

    override suspend fun stopStatisticsSession() {
        withContext(Dispatchers.IO) {
            val current = statisticsDao.getLast() ?: return@withContext
            if (current.dateEnd != null) return@withContext
            val updated = current.copy(dateEnd = LocalDateTime.now())
            statisticsDao.update(updated)
        }
    }

    override suspend fun changeStatisticsSessionName(id: Int?, name: String) {
        withContext(Dispatchers.IO) {
            val current = if (id == null) {
                statisticsDao.getLast() ?: return@withContext
            } else {
                statisticsDao.getById(id) ?: return@withContext
            }
            val updated = current.copy(name = name)
            statisticsDao.update(updated)
        }
    }

    override suspend fun addPulse(pulse: Int) {
        withContext(Dispatchers.IO) {
            val current = statisticsDao.getLast() ?: return@withContext
            val pulseWithTime = pulse to LocalDateTime.now()
            val updated = current.copy(pulse = current.pulse + pulseWithTime)
            statisticsDao.update(updated)
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