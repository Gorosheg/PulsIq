package com.example.storage.statistics

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.storage.dao.StatisticsDao
import com.example.storage.midel.StatisticsEntity

@Database(
    entities = [
        StatisticsEntity::class,
    ], version = 1
)

internal abstract class StatisticsDatabase : RoomDatabase() {

    abstract val statisticsDao: StatisticsDao
}