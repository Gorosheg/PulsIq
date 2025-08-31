package gorosheg.pulsiq.pulse_notification.di

import gorosheg.pulsiq.common.notification.PulseNotificationInitializer
import gorosheg.pulsiq.pulse_notification.PulseNotificationInitializerImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val pulseNotificationModule = module {
    single<PulseNotificationInitializer> {
        PulseNotificationInitializerImpl(androidContext())
    }
}
