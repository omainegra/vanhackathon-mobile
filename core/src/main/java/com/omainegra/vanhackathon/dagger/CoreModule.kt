package com.omainegra.vanhackathon.dagger

import com.omainegra.vanhackathon.services.*
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import javax.inject.Qualifier

/**
 * Created by omainegra on 1/19/18.
 */

@Module
object CoreModule {
    @JvmStatic @Provides @Reusable @IO
    fun provideIOScheduler(): Scheduler = Schedulers.io()

    @JvmStatic @Provides @Reusable @Computation
    fun provideComputationScheduler(): Scheduler = Schedulers.computation()

    @JvmStatic @Provides @Reusable
    fun providePreferences(@IO scheduler: Scheduler, keyValueStore: KeyValueStore): Preferences =
            PreferencesImpl(keyValueStore, scheduler)

    @JvmStatic @Provides @Reusable
    fun provideNetwork(@IO scheduler: Scheduler, directories: Directories): Network =
        NetworkImpl("http://api-vanhack-event-sp.azurewebsites.net/", scheduler, directories)

    @JvmStatic @Provides @Reusable
    fun provideSecurity(network: Network, preferences: Preferences): Security =
            SecurityImpl(network, preferences)
}

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class IO

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class Computation