package com.example.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import gorosheg.pulsiq.common.storage.ThresholdsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class ThresholdsRepositoryImpl(context: Context) : ThresholdsRepository {

    private val prefs: SharedPreferences = context.getSharedPreferences(
        THRESHOLDS,
        Context.MODE_PRIVATE
    )

    private val _lowerThresholdFlow = MutableStateFlow(getLowerThreshold())
    private val _upperThresholdFlow = MutableStateFlow(getUpperThreshold())

    override val lowerThresholdFlow: StateFlow<Int> = _lowerThresholdFlow
    override val upperThresholdFlow: StateFlow<Int> = _upperThresholdFlow

    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
        when (key) {
            KEY_LOWER -> _lowerThresholdFlow.value = getLowerThreshold()
            KEY_UPPER -> _upperThresholdFlow.value = getUpperThreshold()
        }
    }

    init {
        prefs.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun saveThresholds(lower: Int, upper: Int) {
        prefs.edit {
            putInt(KEY_LOWER, lower)
            putInt(KEY_UPPER, upper)
        }
        _lowerThresholdFlow.value = lower
        _upperThresholdFlow.value = upper
    }

    override fun getLowerThreshold(): Int = prefs.getInt(KEY_LOWER, 100)
    override fun getUpperThreshold(): Int = prefs.getInt(KEY_UPPER, 180)

    companion object {
        private const val KEY_LOWER = "lowerThreshold"
        private const val KEY_UPPER = "upperThreshold"
        private const val THRESHOLDS = "thresholds"
    }
}
