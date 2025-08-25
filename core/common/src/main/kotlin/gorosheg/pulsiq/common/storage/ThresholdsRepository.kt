package gorosheg.pulsiq.common.storage

import kotlinx.coroutines.flow.StateFlow

interface ThresholdsRepository {

    val lowerThresholdFlow: StateFlow<Int>
    val upperThresholdFlow: StateFlow<Int>

    fun saveThresholds(lower: Int, upper: Int)
    fun getLowerThreshold(): Int
    fun getUpperThreshold(): Int
}