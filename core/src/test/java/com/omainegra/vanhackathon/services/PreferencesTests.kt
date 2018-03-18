package com.omainegra.vanhackathon.services

import arrow.syntax.option.some
import io.kotlintest.specs.*
import io.kotlintest.matchers.*
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

/**
 * Created by omainegra on 3/18/18.
 */

class PreferencesTests : StringSpec({

    "isFirstTime should return true when there is no value in the key store" {
        val keyValueStore = InMemoryKeyValueStore(hashMapOf())
        val scheduler = TestScheduler()

        val preferences = PreferencesImpl(keyValueStore, scheduler)

        val value = preferences.isFirstTime().test()
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        value.assertValue(true)
        value.assertComplete()
    }

    "isFirstTime should return the value present in the key store" {
        val keyValueStore = InMemoryKeyValueStore(hashMapOf())
        val scheduler = TestScheduler()

        val preferences = PreferencesImpl(keyValueStore, scheduler)

        forAll { someBoolean: Boolean ->
            keyValueStore.putBoolean("FIRST_TIME_PARAM", someBoolean)
            val value = preferences.isFirstTime().test()
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            value.assertValue(someBoolean)
            value.assertComplete()

            true
        }
    }

    "setFirstTime should put a Boolean value into the key store" {
        val keyValueStore = InMemoryKeyValueStore(hashMapOf())
        val scheduler = TestScheduler()

        val preferences = PreferencesImpl(keyValueStore, scheduler)

        forAll { someBoolean: Boolean ->
            val completable = preferences.setFirstTime(someBoolean).test()
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            completable.assertComplete()
            keyValueStore.getBoolean("FIRST_TIME_PARAM") shouldBe someBoolean.some()

            true
        }
    }

    "getAccessToken should return an Optional containing the access token from the key store if present" {
        val keyValueStore = InMemoryKeyValueStore(hashMapOf())
        val scheduler = TestScheduler()

        val preferences = PreferencesImpl(keyValueStore, scheduler)

        forAll { someString: String ->
            keyValueStore.putString("ACCESS_TOKEN_PARAM", someString)

            val value = preferences.getAccessToken().test()
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            value.assertValue { it.nonEmpty() && it.orNull()!! == someString }
            value.assertComplete()

            true
        }
    }

    "getAccessToken should return an Optional containing None from the key store if absent" {
        val keyValueStore = InMemoryKeyValueStore(hashMapOf())
        val scheduler = TestScheduler()

        val preferences = PreferencesImpl(keyValueStore, scheduler)

        val value = preferences.getAccessToken().test()
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        value.assertValue { it.isEmpty() && it.orNull() == null }
        value.assertComplete()
    }

    "setAccessToken should put a String value into the key store" {
        val keyValueStore = InMemoryKeyValueStore(hashMapOf())
        val scheduler = TestScheduler()

        val preferences = PreferencesImpl(keyValueStore, scheduler)

        forAll { someValue: String ->
            val completable = preferences.setAccessToken(someValue).test()
            scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

            completable.assertComplete()
            keyValueStore.getString("ACCESS_TOKEN_PARAM") shouldBe someValue.some()

            true
        }
    }
})