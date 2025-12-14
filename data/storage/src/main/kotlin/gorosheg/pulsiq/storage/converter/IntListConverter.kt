package gorosheg.pulsiq.storage.converter

import androidx.room.TypeConverter
import java.time.LocalDateTime

internal class IntListConverter {

    @TypeConverter
    fun fromList(list: List<Pair<Int, LocalDateTime>>?): String {
        return list?.joinToString(separator = ",") { "${it.first}:${it.second}" } ?: ""
    }

    @TypeConverter
    fun toList(data: String?): List<Pair<Int, LocalDateTime>> {
        if (data.isNullOrEmpty()) return emptyList()
        return data.split(',')
            .filter { it.isNotEmpty() }
            .mapNotNull { token ->
                val parts = token.split(':', limit = 2)
                val first = parts.getOrNull(0)?.toIntOrNull()
                val dateStr = parts.getOrNull(1)
                val second = try {
                    dateStr?.let { LocalDateTime.parse(it) }
                } catch (_: Exception) {
                    null
                }
                if (first != null && second != null) first to second else null
            }
    }
}