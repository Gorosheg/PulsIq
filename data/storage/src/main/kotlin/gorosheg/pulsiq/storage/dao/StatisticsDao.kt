package gorosheg.pulsiq.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import gorosheg.pulsiq.storage.midel.StatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface StatisticsDao {

    @Transaction
    suspend fun createStatisticsSession() {
        insert(StatisticsEntity(dateStart = System.currentTimeMillis()))
    }

    suspend fun stopStatisticsSession() {
        val current = getLast() ?: return
        val updated = current.copy(dateEnd = System.currentTimeMillis())
        update(updated)
    }

    @Transaction
    suspend fun addPulseToCurrent(pulse: Int) {
        val current = getLast() ?: return
        val updated = current.copy(pulse = current.pulse + pulse)
        update(updated)
    }

    @Query("SELECT * FROM StatisticsEntity")
    fun getPulseList(): Flow<List<StatisticsEntity>>

    @Query("SELECT * FROM StatisticsEntity WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): StatisticsEntity?

    @Query("DELETE FROM StatisticsEntity WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM StatisticsEntity")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: StatisticsEntity)

    @Query("SELECT * FROM StatisticsEntity ORDER BY dateStart DESC LIMIT 1")
    suspend fun getLast(): StatisticsEntity?

    @Update
    suspend fun update(entity: StatisticsEntity)
}