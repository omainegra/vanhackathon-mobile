package com.omainegra.vanhackathon.services

import arrow.core.Option
import arrow.core.getOrElse
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by omainegra on 3/18/18.
 */
interface Preferences {
    fun isFirstTime(): Observable<Boolean>
    fun setFirstTime(value: Boolean): Observable<Unit>

    fun getAccessToken(): Observable<Option<String>>
    fun setAccessToken(value: String): Observable<Unit>
}

class PreferencesImpl(
        private val keyValueStore: KeyValueStore,
        private val scheduler: Scheduler): Preferences {

    companion object {
        const val firstTimeParam = "FIRST_TIME_PARAM"
        const val accessTokenParam = "ACCESS_TOKEN_PARAM"
    }

    override fun isFirstTime(): Observable<Boolean> =
        Observable.fromCallable {
            keyValueStore.getBoolean(firstTimeParam).getOrElse { true }
        }.subscribeOn(scheduler)

    override fun setFirstTime(value: Boolean): Observable<Unit> =
        Observable.fromCallable {
            keyValueStore.putBoolean(firstTimeParam, value)
        }.subscribeOn(scheduler)

    override fun getAccessToken(): Observable<Option<String>> =
        Observable.fromCallable {
            keyValueStore.getString(accessTokenParam)
        }.subscribeOn(scheduler)

    override fun setAccessToken(value: String): Observable<Unit> =
        Observable.fromCallable {
            keyValueStore.putString(accessTokenParam, value)
        }.subscribeOn(scheduler)
}