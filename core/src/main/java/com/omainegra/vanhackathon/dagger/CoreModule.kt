package com.omainegra.vanhackathon.dagger

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
}

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class IO

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class Computation