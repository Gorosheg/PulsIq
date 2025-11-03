package gorosheg.pulsiq.storage.midel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
internal data class StatisticsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dateStart: Long,
    val dateEnd: Long? = null,
    val name: String = "Тренировка",
    val pulse: List<Pair<Int, Long>> = emptyList(),
)