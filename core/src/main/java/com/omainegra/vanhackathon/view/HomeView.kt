package com.omainegra.vanhackathon.view

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import org.slf4j.LoggerFactory

/**
 * Created by omainegra on 2/21/18.
 */

sealed class HomeNavigation : Navigation

interface HomeViewModel : ViewModel<HomeNavigation>

class HomeViewModelImpl(private val scheduler: Scheduler): HomeViewModel {

    private val log = LoggerFactory.getLogger(javaClass)
    private val disposable = CompositeDisposable()

    override fun start() {
        log.info("HomeViewModelImpl started")
    }

    override fun navigation(): Observable<HomeNavigation> = Observable.empty()

    override fun stop() {
        log.info("HomeViewModelImpl stopped")
        disposable.dispose()
    }
}