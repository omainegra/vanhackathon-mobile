package com.omainegra.vanhackathon.dagger

import dagger.BindsInstance
import dagger.Component
import com.omainegra.vanhackathon.AndroidApp
import javax.inject.Singleton

/**
 * Created by omainegra on 2/23/18.
 */

@Singleton
@Component(modules = [CoreModule::class, AndroidModule::class])
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: AndroidApp): Builder
        fun build(): AppComponent
    }

    fun activityComponentBuilder(): ActivityComponent.Builder
}