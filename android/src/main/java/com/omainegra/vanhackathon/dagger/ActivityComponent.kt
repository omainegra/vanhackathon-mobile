package com.omainegra.vanhackathon.dagger

import android.app.Activity
import android.app.Application
import android.os.Bundle
import dagger.Subcomponent
import com.omainegra.vanhackathon.AndroidApp
import com.omainegra.vanhackathon.view.HomeActivity
import com.omainegra.vanhackathon.view.RegisterActivity
import com.omainegra.vanhackathon.view.SplashActivity
import org.slf4j.LoggerFactory
import javax.inject.Scope

/**
 * Created by omainegra on 2/23/18.
 */

@ActivityScope
@Subcomponent(modules = [ViewModule::class])
interface ActivityComponent {
    fun inject(activity: SplashActivity)
    fun inject(activity: RegisterActivity)
    fun inject(activity: HomeActivity)

    @Subcomponent.Builder
    interface Builder {
        fun build(): ActivityComponent
    }
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ActivityScope

class ActivityScopeManager(application: AndroidApp) {
    private val log = LoggerFactory.getLogger(javaClass)
    private val components: HashMap<String, ActivityComponent> = HashMap()

    init {
        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks{
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                val className = activity::class.java.canonicalName
                log.info("Activity created %s".format(className))
                if (!components.contains(className)){
                    components[className] = application.applicationComponent.activityComponentBuilder().build()
                }
            }

            override fun onActivityDestroyed(activity: Activity) {
                val className = activity::class.java.canonicalName
                if (activity.isFinishing) {
                    log.info("Activity destroyed %s".format(className))
                    components -= className
                }
                else
                    log.info("Activity rotated %s".format(className))
            }

            override fun onActivityPaused(activity: Activity?) { }
            override fun onActivityResumed(activity: Activity?) { }
            override fun onActivityStarted(activity: Activity?) { }
            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) { }
            override fun onActivityStopped(activity: Activity?) { }
        })
    }

    fun getComponent(activity: Activity) = components[activity::class.java.canonicalName]
}