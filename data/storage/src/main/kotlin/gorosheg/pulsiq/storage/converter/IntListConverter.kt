package gorosheg.pulsiq.storage.converter

import androidx.room.TypeConverter

internal class IntListConverter {

    @TypeConverter
    fun fromList(list: List<Int>?): String {
        return list?.joinToString(separator = ",") ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<Int> {
        if (data.isNullOrEmpty()) return emptyList()
        return data.split(',')
            .filter { it.isNotEmpty() }
            .mapNotNull { it.toIntOrNull() }
    }
}