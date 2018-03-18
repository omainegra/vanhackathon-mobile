package com.omainegra.vanhackathon.dagger

import com.omainegra.vanhackathon.IosApplication
import com.omainegra.vanhackathon.services.*
import dagger.Module
import dagger.Provides
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.robovm.apple.foundation.NSOperationQueue

/**
 * Created by omainegra on 2/9/18.
 */

@Module
object IosModule {

    @JvmStatic @Provides
    fun provideInitializer(application: IosApplication): Initializer = application

    @JvmStatic @Provides
    fun provideKeyValueStore(): KeyValueStore = IosKeyValueStore()

    @JvmStatic @Provides
    fun provideDirectories(): Directories = IosDirectories()

    @JvmStatic @Provides @Main
    fun provideMainScheduler(): Scheduler = Schedulers.from({ action ->
        NSOperationQueue.getMainQueue().addOperation(action)
    })
}