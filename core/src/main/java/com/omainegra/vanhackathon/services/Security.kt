package com.omainegra.vanhackathon.services

import arrow.core.Either
import arrow.syntax.either.left
import arrow.syntax.either.right
import com.github.salomonbrys.kotson.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.omainegra.vanhackathon.model.Customer
import com.omainegra.vanhackathon.model.NewCustomer
import io.reactivex.Observable
import io.reactivex.Scheduler
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.slf4j.LoggerFactory
import org.threeten.bp.ZonedDateTime
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.atomic.AtomicLong


/**
 * Created by omainegra on 3/13/18.
 */

interface Security {
    fun createCustomer(customer: NewCustomer): Observable<Either<Throwable, Unit>>
    fun authenticate(username: String, password: String): Observable<Either<Throwable, Unit>>
}

class SecurityImpl(
    private val network: Network,
    private val preferences: Preferences) : Security {

    private val log = LoggerFactory.getLogger(javaClass)

    override fun createCustomer(customer: NewCustomer): Observable<Either<Throwable, Unit>> =
        network.createCustomer(customer)
            .flatMap(::saveAccessToken)

    override fun authenticate(username: String, password: String): Observable<Either<Throwable, Unit>> =
        network.authenticate(username, password)
            .flatMap(::saveAccessToken)

    private fun saveAccessToken(accessToken: Either<Throwable, String>): Observable<Either<Throwable, Unit>> {
        return accessToken.fold(
                { Observable.just(it.left())},
                { preferences.setAccessToken(it).map { Unit.right() } })
    }
}