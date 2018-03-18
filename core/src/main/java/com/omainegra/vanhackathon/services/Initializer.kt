package com.omainegra.vanhackathon.services

import io.reactivex.Observable

/**
 * Created by omainegra on 3/5/18.
 */

interface Initializer {
    fun initialize(): Observable<Unit> = initializeTimeZoneDatabase()

    fun initializeTimeZoneDatabase(): Observable<Unit>
}

class NoOpInitializer : Initializer {

    override fun initializeTimeZoneDatabase(): Observable<Unit> = Observable.just(Unit)

}