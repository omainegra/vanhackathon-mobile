package com.omainegra.vanhackathon.view

import com.omainegra.vanhackathon.model.Product
import com.omainegra.vanhackathon.model.Store
import com.omainegra.vanhackathon.services.Network
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.slf4j.LoggerFactory

/**
 * Created by omainegra on 2/21/18.
 */

sealed class HomeNavigation : Navigation

interface HomeViewModel : ViewModel<HomeNavigation>

class HomeViewModelImpl(
    private val network: Network,
    private val scheduler: Scheduler): HomeViewModel {

    private val log = LoggerFactory.getLogger(javaClass)
    private val disposable = CompositeDisposable()

    override fun start() {
        log.info("HomeViewModelImpl started")

        network.getStores()
                .concatMap {
                    it.fold(
                        { error ->
                            log.error("Error getting stores", error)
                            Observable.empty<Store>()
                        },
                        { Observable.fromIterable(it) })
                }
                .doOnNext { log.info("Got: $it") }
                .concatMap(network::getProducts)
                .doOnNext { log.info("Got: $it") }
                .subscribe()
                .addTo(disposable)
    }

    override fun navigation(): Observable<HomeNavigation> = Observable.empty()

    override fun stop() {
        log.info("HomeViewModelImpl stopped")
        disposable.dispose()
    }
}