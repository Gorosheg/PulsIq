package com.example.storage

import kotlinx.coroutines.flow.Flow

interface ThresholdsRepository {

    val lowerThresholdFlow: Flow<Int>

    val upperThresholdFlow: Flow<Int>

    fun saveThresholds(lower: Int, upper: Int)

    fun getLowerThreshold(): Int

    fun getUpperThreshold(): Int
}