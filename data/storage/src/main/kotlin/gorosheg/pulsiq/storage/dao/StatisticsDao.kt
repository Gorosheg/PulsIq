package gorosheg.pulsiq.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.Companion.IGNORE
import androidx.room.Query
import gorosheg.pulsiq.storage.midel.StatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
internal interface StatisticsDao {

    @Insert(onConflict = IGNORE)
    suspend fun addPulse(pulse: StatisticsEntity)

    @Query("SELECT * FROM StatisticsEntity")
    fun getPulseList(): Flow<List<StatisticsEntity>>

    @Query("DELETE FROM StatisticsEntity")
    suspend fun clearAll()
}