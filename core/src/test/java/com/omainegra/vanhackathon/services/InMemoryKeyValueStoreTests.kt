package com.omainegra.vanhackathon.services

import io.kotlintest.matchers.beOfType
import io.kotlintest.matchers.should
import io.kotlintest.matchers.shouldBe
import io.kotlintest.matchers.shouldNotBe
import io.kotlintest.properties.forAll
import io.kotlintest.specs.StringSpec

/**
 * Created by omainegra on 2/22/18.
 */

class InMemoryKeyValueStoreTests : StringSpec({

    "putString should add a value of type String to the key store" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        forAll { someKey: String, someValue: String ->
            keyValueStore.putString(someKey, someValue)

            map[someKey] shouldNotBe null
            map[someKey]!! should beOfType<String>()
            map[someKey]!! shouldBe someValue

            true
        }
    }

    "getString should return an Optional containing the value of String from the key store if present" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        forAll { someKey: String, someValue: String ->
            map[someKey] = someValue
            val ret = keyValueStore.getString(someKey)

            ret.isDefined() shouldBe true
            ret.orNull()!! shouldBe someValue

            true
        }
    }

    "getString should return an Optional containing None from the key store if the value is not a String" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        map["SOME_KEY"] = 1
        val ret = keyValueStore.getString("SOME_KEY")

        ret.isEmpty() shouldBe true
        ret.orNull() shouldBe null
    }

    "getString should return an Optional containing None from the key store if absent" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        val ret = keyValueStore.getString("SOME_KEY")

        ret.isEmpty() shouldBe true
        ret.orNull() shouldBe null
    }

    "putBoolean should add a value of type Boolean to the key store" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        forAll { someKey: String, someValue: Boolean ->
            keyValueStore.putBoolean(someKey, someValue)

            map[someKey] shouldNotBe null
            map[someKey]!! should beOfType<Boolean>()
            map[someKey]!! shouldBe someValue

            true
        }

    }

    "getBoolean should return an Optional containing the value of Boolean from the key store if present" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        forAll { someKey: String, someValue: Boolean ->
            map[someKey] = someValue
            val ret = keyValueStore.getBoolean(someKey)

            ret.isDefined() shouldBe true
            ret.orNull()!! shouldBe someValue

            true
        }
    }

    "getBoolean should return an Optional containing None from the key store if absent" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        val ret = keyValueStore.getBoolean("SOME_KEY")

        ret.isEmpty() shouldBe true
        ret.orNull() shouldBe null
    }

    "getBoolean should return an Optional containing None from the key store if the value is not a Boolean" {
        val map = hashMapOf<String, Any>()
        val keyValueStore = InMemoryKeyValueStore(map)

        map["SOME_KEY"] = 1
        val ret = keyValueStore.getBoolean("SOME_KEY")

        ret.isEmpty() shouldBe true
        ret.orNull() shouldBe null
    }
})