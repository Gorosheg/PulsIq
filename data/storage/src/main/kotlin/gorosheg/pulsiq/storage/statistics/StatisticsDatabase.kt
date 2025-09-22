package gorosheg.pulsiq.storage.statistics

import androidx.room.Database
import androidx.room.RoomDatabase
import gorosheg.pulsiq.storage.dao.StatisticsDao
import gorosheg.pulsiq.storage.midel.StatisticsEntity

@Database(
    entities = [
        StatisticsEntity::class,
    ], version = 1
)

internal abstract class StatisticsDatabase : RoomDatabase() {

    abstract val statisticsDao: StatisticsDao
}