package com.omainegra.vanhackathon

import com.omainegra.vanhackathon.dagger.DaggerIosComponent
import com.omainegra.vanhackathon.dagger.IosComponent
import com.omainegra.vanhackathon.services.Initializer
import io.reactivex.Observable
import org.robovm.apple.foundation.NSAutoreleasePool
import org.robovm.apple.foundation.NSBundle
import org.robovm.apple.uikit.UIApplication
import org.robovm.apple.uikit.UIApplicationDelegateAdapter
import org.robovm.apple.uikit.UIApplicationLaunchOptions
import org.robovm.apple.uikit.UIViewController
import org.slf4j.LoggerFactory
import org.threeten.bp.zone.TzdbZoneRulesProvider
import org.threeten.bp.zone.ZoneRulesProvider
import java.io.FileInputStream


/**
 * Created by omainegra on 1/12/18.
 */

class IosApplication : UIApplicationDelegateAdapter(), Initializer {

    lateinit var component: IosComponent
    private val log = LoggerFactory.getLogger(javaClass)

    override fun didFinishLaunching(application: UIApplication?, launchOptions: UIApplicationLaunchOptions?): Boolean {
        log.info("IosApplication initialized")
        log.info("OS name: ${System.getProperty("os.name")}, version: ${System.getProperty("os.version")}")

        component = DaggerIosComponent.builder().application(this).build()

        return true
    }

    override fun initializeTimeZoneDatabase(): Observable<Unit> = Observable.fromCallable {
        val file = NSBundle.getMainBundle().findResourcePath("TZDB", "dat")
        FileInputStream(file).use {
            ZoneRulesProvider.registerProvider(TzdbZoneRulesProvider(it))
        }
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            NSAutoreleasePool().use {
                UIApplication.main<UIApplication, IosApplication>(args, null, IosApplication::class.java)
            }
        }
    }
}

val UIViewController.injector: IosComponent
    get() = (UIApplication.getSharedApplication().delegate as IosApplication).component