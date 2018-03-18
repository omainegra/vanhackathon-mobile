package com.omainegra.vanhackathon.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import com.omainegra.vanhackathon.AndroidApp
import com.omainegra.vanhackathon.services.*
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

/**
 * Created by omainegra on 2/22/18.
 */

@Module
object AndroidModule {

    @JvmStatic @Provides
    fun provideSharedPreferences(application: AndroidApp): SharedPreferences =
        application.getSharedPreferences("VanHackKeyValueStore", Context.MODE_PRIVATE)

    @JvmStatic @Provides
    fun provideKeyValueStore(sharedPreferences: SharedPreferences): KeyValueStore =
        AndroidKeyValueStore(sharedPreferences)

    @JvmStatic @Provides
    fun provideInitializer(application: AndroidApp): Initializer = application

    @JvmStatic @Provides
    fun provideDirectories(application: AndroidApp): Directories = AndroidDirectories(application)

    @JvmStatic @Provides @Main
    fun provideMainScheduler(): Scheduler = AndroidSchedulers.mainThread()
}