package gorosheg.pulsiq.storage.mapper

import gorosheg.pulsiq.common.model.PulseStatistic
import gorosheg.pulsiq.storage.midel.StatisticsEntity

internal fun List<StatisticsEntity>.toDomain(): List<PulseStatistic> {
    return map { it.toDomain() }
}

internal fun StatisticsEntity.toDomain(): PulseStatistic {
    return PulseStatistic(
        id = id,
        dateStart = dateStart,
        dateEnd = dateEnd,
        name = name,
        pulse = pulse,
    )
}