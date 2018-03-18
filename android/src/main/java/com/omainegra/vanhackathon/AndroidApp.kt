package com.omainegra.vanhackathon

import android.support.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.squareup.leakcanary.LeakCanary
import com.omainegra.vanhackathon.dagger.ActivityScopeManager
import com.omainegra.vanhackathon.dagger.AppComponent
import com.omainegra.vanhackathon.dagger.DaggerAppComponent
import com.omainegra.vanhackathon.services.Initializer
import io.reactivex.Observable
import org.slf4j.LoggerFactory
import org.threeten.bp.zone.TzdbZoneRulesProvider
import org.threeten.bp.zone.ZoneRulesProvider

/**
 * Created by omainegra on 2/21/18.
 */

class AndroidApp : MultiDexApplication(), Initializer{

    private val log = LoggerFactory.getLogger(javaClass)

    lateinit var applicationComponent: AppComponent
    lateinit var activityScopeManager: ActivityScopeManager

    override fun onCreate() {
        super.onCreate()

        // Install LeakCanary
        if (LeakCanary.isInAnalyzerProcess(this)) return
        LeakCanary.install(this)

        // Initialize Fresco
        Fresco.initialize(this)

        // Initialize Dagger
        applicationComponent = DaggerAppComponent.builder().application(this).build()
        activityScopeManager = ActivityScopeManager(this)
    }

    override fun initializeTimeZoneDatabase(): Observable<Unit> = Observable.fromCallable {
        log.info("Load TimeZone database from Android assets")
        assets.open("TZDB.dat").use { ZoneRulesProvider.registerProvider(TzdbZoneRulesProvider(it)) }
    }

}