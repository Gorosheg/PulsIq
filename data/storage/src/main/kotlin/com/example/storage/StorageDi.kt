package com.example.storage

import gorosheg.pulsiq.common.storage.ThresholdsRepository
import org.koin.dsl.module

val storageModule = module {

    single<ThresholdsRepository> { ThresholdsRepositoryImpl(get()) }
}