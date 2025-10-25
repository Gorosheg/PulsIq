package gorosheg.pulsiq.storage.mapper

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.storage.midel.StatisticsEntity


internal fun List<StatisticsEntity>.toDomain(): List<PulseStatistic> {
    return map { it.toDomain() }
}

internal fun StatisticsEntity.toDomain(): PulseStatistic {
    return PulseStatistic(
        id = this.id,
        dateStart = this.dateStart,
        dateEnd = this.dateEnd,
        name = this.name,
        pulse = this.pulse,
    )
}