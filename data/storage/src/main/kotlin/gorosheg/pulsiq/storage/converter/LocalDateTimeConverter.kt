package gorosheg.pulsiq.storage.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal class LocalDateTimeConverter {

    @TypeConverter
    fun fromLong(value: Long?): LocalDateTime? {
        if (value == null) return null
        return Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalDateTime()
    }

    @TypeConverter
    fun toLong(dateTime: LocalDateTime?): Long? {
        if (dateTime == null) return null
        return dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}