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

interface Network {
    fun createCustomer(customer: NewCustomer): Observable<Either<Throwable, String>>
    fun authenticate(username: String, password: String): Observable<Either<Throwable, String>>
}

class NetworkImpl(
    private val baseUrl: String,
    private val scheduler: Scheduler,
    private val directories: Directories) : Network {

    private val log = LoggerFactory.getLogger(javaClass)

    private val customerApi: CustomerApi
    private val gson: Gson

    init {
        gson = GsonBuilder()
                .registerTypeAdapter(JsonMapping.newCustomerSerializer)
                .registerTypeAdapter(JsonMapping.zonedDateTimeDeserializer)
                .create()

        val logging = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { log.info(it) })
        logging.level = HttpLoggingInterceptor.Level.BODY

        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.addInterceptor(logging)
        clientBuilder.cache(Cache(directories.cache(), 10 * 1024 * 1024 /* 10 MB */ ))

        val retrofit = Retrofit.Builder()
                .client(clientBuilder.build())
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        customerApi = retrofit.create(CustomerApi::class.java)
    }

    override fun createCustomer(customer: NewCustomer): Observable<Either<Throwable, String>> {
        return customerApi.create(customer)
                .map { it.right() as  Either<Throwable, String>}
                .onErrorReturn { it.left() }
                .subscribeOn(scheduler)
    }

    override fun authenticate(username: String, password: String): Observable<Either<Throwable, String>> {
        return customerApi.authenticate(username, password)
                .map { it.right() as  Either<Throwable, String>}
                .onErrorReturn { it.left() }
                .subscribeOn(scheduler)
    }
}

interface CustomerApi {
    @POST("api/v1/Customer")
    fun create(@Body customer: NewCustomer): Observable<String>

    @POST("api/v1/Customer")
    fun authenticate(@Query("emailChanged") email: String, @Query("passwordChanged") password: String): Observable<String>
}

object JsonMapping {

    val newCustomerSerializer = jsonSerializer<Customer> {
        jsonObject(
            "name" to it.src.name.toJson(),
            "address" to it.src.address.toJson(),
            "email" to it.src.email.toJson(),
            "password" to it.src.password.toJson()
        )
    }

    val zonedDateTimeDeserializer = jsonDeserializer {
        ZonedDateTime.parse(it.json.asString)
    }
}