 package com.example.storage

import android.content.Context
import androidx.core.content.edit

 class ThresholdsRepository(context: Context) {
    private val prefs = context.getSharedPreferences("thresholds", Context.MODE_PRIVATE)

    fun saveThresholds(lower: Int, upper: Int) {
        prefs.edit {
            putInt("lowerThreshold", lower)
                .putInt("upperThreshold", upper)
        }
    }

    fun getLowerThreshold(): Int = prefs.getInt("lowerThreshold", 100)
    fun getUpperThreshold(): Int = prefs.getInt("upperThreshold", 180)
}