package com.example.storage.mapper

import com.example.storage.midel.StatisticsEntity

internal fun List<StatisticsEntity>.toDomain(): List<Int> {
    return map(StatisticsEntity::toDomain)
}

internal fun Int.toEntity(): StatisticsEntity {
    return StatisticsEntity(pulse = this)
}

private fun StatisticsEntity.toDomain(): Int {
    return this.pulse
}