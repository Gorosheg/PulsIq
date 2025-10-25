package gorosheg.pulsiq.storage.statistics

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import gorosheg.pulsiq.storage.converter.IntListConverter
import gorosheg.pulsiq.storage.dao.StatisticsDao
import gorosheg.pulsiq.storage.midel.StatisticsEntity

@Database(
    entities = [
        StatisticsEntity::class,
    ], version = 1
)
@TypeConverters(IntListConverter::class)
internal abstract class StatisticsDatabase : RoomDatabase() {

    abstract val statisticsDao: StatisticsDao
}