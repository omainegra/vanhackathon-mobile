package com.omainegra.vanhackathon.view

import com.omainegra.vanhackathon.services.Initializer
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import org.slf4j.LoggerFactory

/**
 * Created by omainegra on 2/21/18.
 */

sealed class SplashNavigation : Navigation {
    object Home : SplashNavigation()
}

interface SplashViewModel : ViewModel<SplashNavigation>

class SplashViewModelImpl(
    private val scheduler: Scheduler,
    private val initializer: Initializer): SplashViewModel {

    private val log = LoggerFactory.getLogger(javaClass)

    private val disposable = CompositeDisposable()
    private val navigationSubject = BehaviorSubject.create<SplashNavigation>()

    override fun start() {
        log.info("SplashViewModelImpl started")

        initializer.initialize()
            .map { SplashNavigation.Home }
            .subscribe(navigationSubject::onNext)
            .addTo(disposable)
    }

    override fun navigation(): Observable<SplashNavigation> = navigationSubject.observeOn(scheduler)

    override fun stop() {
        log.info("SplashViewModelImpl stopped")
        disposable.dispose()
    }
}