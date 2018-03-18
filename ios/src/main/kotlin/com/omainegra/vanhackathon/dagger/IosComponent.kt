package com.omainegra.vanhackathon.dagger

import com.omainegra.vanhackathon.IosApplication
import com.omainegra.vanhackathon.controllers.HomeViewController
import dagger.Component
import com.omainegra.vanhackathon.controllers.SplashViewController
import dagger.BindsInstance
import javax.inject.Singleton

/**
 * Created by omainegra on 2/6/18.
 */

@Singleton
@Component(modules = [CoreModule::class, ViewModule::class, IosModule::class])
interface IosComponent {
    fun inject(controller: SplashViewController)
    fun inject(controller: HomeViewController)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: IosApplication): Builder
        fun build(): IosComponent
    }
}