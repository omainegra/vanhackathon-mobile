package com.omainegra.vanhackathon.view

import com.omainegra.vanhackathon.services.Initializer
import com.omainegra.vanhackathon.services.Preferences
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import org.slf4j.LoggerFactory

/**
 * Created by omainegra on 2/21/18.
 */

sealed class SplashNavigation : Navigation {
    object Register : SplashNavigation()
    object Home : SplashNavigation()
}

interface SplashViewModel : ViewModel<SplashNavigation>

class SplashViewModelImpl(
    private val scheduler: Scheduler,
    private val initializer: Initializer,
    private val preferences: Preferences): SplashViewModel {

    private val log = LoggerFactory.getLogger(javaClass)

    private val disposable = CompositeDisposable()
    private val navigationSubject = BehaviorSubject.create<SplashNavigation>()

    override fun start() {
        log.info("SplashViewModelImpl started")

        initializer.initialize()
            .flatMap { preferences.getAccessToken() }
            .map { it.fold({ SplashNavigation.Register }, { SplashNavigation.Home }) }
            .subscribe(navigationSubject::onNext)
            .addTo(disposable)
    }

    override fun navigation(): Observable<SplashNavigation> = navigationSubject.observeOn(scheduler)

    override fun stop() {
        log.info("SplashViewModelImpl stopped")
        disposable.dispose()
    }
}