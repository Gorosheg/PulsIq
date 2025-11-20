package gorosheg.pulsiq.storage.midel

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
internal data class StatisticsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateStart: LocalDateTime,
    val dateEnd: LocalDateTime? = null,
    val name: String = "",
    val pulse: List<Pair<Int, LocalDateTime>> = emptyList(),
)