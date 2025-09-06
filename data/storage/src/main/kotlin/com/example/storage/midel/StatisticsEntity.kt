package com.example.storage.midel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal class StatisticsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pulse: Int,
)