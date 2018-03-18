package com.omainegra.vanhackathon.view

import com.omainegra.vanhackathon.model.Product
import com.omainegra.vanhackathon.model.Store
import com.omainegra.vanhackathon.services.Network
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory

/**
 * Created by omainegra on 2/21/18.
 */

sealed class HomeNavigation : Navigation

interface HomeViewModel : ViewModel<HomeNavigation> {
    fun storesLce(): Observable<LCE<List<Store>>>
    fun pullToRefresh(): Observer<Unit>
}

class HomeViewModelImpl(
    private val network: Network,
    private val scheduler: Scheduler): HomeViewModel {

    private val log = LoggerFactory.getLogger(javaClass)
    private val disposable = CompositeDisposable()

    private val storesLceSubject = BehaviorSubject.create<LCE<List<Store>>>()
    private val pullToRefreshSubject = PublishSubject.create<Unit>()

    override fun start() {
        log.info("HomeViewModelImpl started")

        pullToRefreshSubject.startWith(Unit)
            .switchMap { network.getStores() }
            .map {
                it.fold({ Error("Error loading stores") }, { Content(it) as LCE<List<Store>> })
            }
            .startWith(Loading)
            .observeOn(scheduler)
            .subscribe(storesLceSubject::onNext)
            .addTo(disposable)
    }

    override fun storesLce(): Observable<LCE<List<Store>>> = storesLceSubject
    override fun navigation(): Observable<HomeNavigation> = Observable.empty()

    override fun pullToRefresh(): Observer<Unit> = pullToRefreshSubject

    override fun stop() {
        log.info("HomeViewModelImpl stopped")
        disposable.dispose()
    }
}