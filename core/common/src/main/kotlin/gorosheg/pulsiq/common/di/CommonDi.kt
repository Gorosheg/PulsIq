package gorosheg.pulsiq.common.di

import gorosheg.pulsiq.common.navigation.NavigatorHolder
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val commonModule = module {
    singleOf(::NavigatorHolder)
}