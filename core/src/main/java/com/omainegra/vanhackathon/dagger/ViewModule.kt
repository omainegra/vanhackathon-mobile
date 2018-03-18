package com.omainegra.vanhackathon.dagger

import dagger.Module
import dagger.Provides
import dagger.Reusable
import com.omainegra.vanhackathon.services.Initializer
import com.omainegra.vanhackathon.services.Network
import com.omainegra.vanhackathon.services.Preferences
import com.omainegra.vanhackathon.services.Security
import com.omainegra.vanhackathon.view.*
import io.reactivex.Scheduler
import javax.inject.Qualifier

/**
 * Created by omainegra on 1/19/18.
 */

@Module
object ViewModule {
    @JvmStatic @Provides @Reusable
    fun provideSplashViewModel(initializer: Initializer, @Main scheduler: Scheduler, preferences: Preferences): SplashViewModel =
        SplashViewModelImpl(scheduler, initializer, preferences)

    @JvmStatic @Provides @Reusable
    fun provideRegisterViewModel(@Main scheduler: Scheduler, security: Security): RegisterViewModel =
        RegisterViewModelImpl(scheduler, security)

    @JvmStatic @Provides @Reusable
    fun provideHomeViewModel(@Main scheduler: Scheduler, network: Network): HomeViewModel =
        HomeViewModelImpl(network, scheduler)
}

@Qualifier @Retention(AnnotationRetention.RUNTIME)
annotation class Main
