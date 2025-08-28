package gorosheg.pulsiq.pulsenotification.di

import gorosheg.pulsiq.common.notification.PulseNotificationInitializer
import gorosheg.pulsiq.pulsenotification.PulseNotificationInitializerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val pulseNotificationModule = module {
    single<PulseNotificationInitializer> {
        PulseNotificationInitializerImpl(androidContext())
    }
}
