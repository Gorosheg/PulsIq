package gorosheg.pulsiq.storage.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.TypeConverters
import androidx.room.Update
import gorosheg.pulsiq.storage.converter.LocalDateTimeConverter
import gorosheg.pulsiq.storage.midel.StatisticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
@TypeConverters(LocalDateTimeConverter::class)
internal interface StatisticsDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: StatisticsEntity)

    @Update
    suspend fun update(entity: StatisticsEntity)

    @Query("SELECT * FROM StatisticsEntity WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): StatisticsEntity?

    @Query("SELECT * FROM StatisticsEntity")
    fun getPulseList(): Flow<List<StatisticsEntity>>

    @Query("SELECT * FROM StatisticsEntity ORDER BY dateStart DESC LIMIT 1")
    suspend fun getLast(): StatisticsEntity?

    @Query("DELETE FROM StatisticsEntity WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM StatisticsEntity")
    suspend fun clearAll()
}