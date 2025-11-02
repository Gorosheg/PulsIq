package gorosheg.pulsiq.storage.converter

import androidx.room.TypeConverter

internal class IntListConverter {

    @TypeConverter
    fun fromList(list: List<Pair<Int, Long>>?): String {
        return list?.joinToString(separator = ",") { "${it.first}:${it.second}" } ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<Pair<Int, Long>> {
        if (data.isNullOrEmpty()) return emptyList()
        return data.split(',')
            .filter { it.isNotEmpty() }
            .mapNotNull { token ->
                val parts = token.split(':')
                val first = parts.getOrNull(0)?.toIntOrNull()
                val second = parts.getOrNull(1)?.toLongOrNull()
                if (first != null && second != null) first to second else null
            }
    }
}