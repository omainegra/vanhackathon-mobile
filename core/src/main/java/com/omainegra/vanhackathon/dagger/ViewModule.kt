package com.omainegra.vanhackathon.dagger

import dagger.Module
import dagger.Provides
import dagger.Reusable
import com.omainegra.vanhackathon.services.Initializer
import com.omainegra.vanhackathon.view.HomeViewModel
import com.omainegra.vanhackathon.view.HomeViewModelImpl
import com.omainegra.vanhackathon.view.SplashViewModel
import com.omainegra.vanhackathon.view.SplashViewModelImpl
import io.reactivex.Scheduler
import javax.inject.Qualifier

/**
 * Created by omainegra on 1/19/18.
 */

@Module
object ViewModule {
    @JvmStatic @Provides @Reusable
    fun provideSplashViewModel(initializer: Initializer, @Main scheduler: Scheduler): SplashViewModel =
            SplashViewModelImpl(scheduler, initializer)

    @JvmStatic @Provides @Reusable
    fun provideHomeViewModel(@Main scheduler: Scheduler): HomeViewModel =
            HomeViewModelImpl(scheduler)
}

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class Main
