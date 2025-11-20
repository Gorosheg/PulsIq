package gorosheg.pulsiq.storage.converter

import androidx.room.TypeConverter
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

internal class LocalDateConverter {

    @TypeConverter
    fun fromLong(value: Long?): LocalDate? {
        if (value == null) return null
        return Instant.ofEpochMilli(value)
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }

    @TypeConverter
    fun toLong(date: LocalDate?): Long? {
        if (date == null) return null
        return date.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
    }
}