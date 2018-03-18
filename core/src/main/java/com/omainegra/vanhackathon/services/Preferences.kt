package com.omainegra.vanhackathon.services

import arrow.core.Option
import arrow.core.getOrElse
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.Single

/**
 * Created by omainegra on 3/18/18.
 */
interface Preferences {
    fun isFirstTime(): Single<Boolean>
    fun setFirstTime(value: Boolean): Completable

    fun getAccessToken(): Single<Option<String>>
    fun setAccessToken(value: String): Completable
}

class PreferencesImpl(
        private val keyValueStore: KeyValueStore,
        private val scheduler: Scheduler): Preferences {

    companion object {
        const val firstTimeParam = "FIRST_TIME_PARAM"
        const val accessTokenParam = "ACCESS_TOKEN_PARAM"
    }

    override fun isFirstTime(): Single<Boolean> =
        Single.fromCallable {
            keyValueStore.getBoolean(firstTimeParam).getOrElse { true }
        }.subscribeOn(scheduler)

    override fun setFirstTime(value: Boolean): Completable =
        Completable.fromAction {
            keyValueStore.putBoolean(firstTimeParam, value)
        }.subscribeOn(scheduler)

    override fun getAccessToken(): Single<Option<String>> =
        Single.fromCallable {
            keyValueStore.getString(accessTokenParam)
        }.subscribeOn(scheduler)

    override fun setAccessToken(value: String): Completable =
        Completable.fromAction {
            keyValueStore.putString(accessTokenParam, value)
        }.subscribeOn(scheduler)
}