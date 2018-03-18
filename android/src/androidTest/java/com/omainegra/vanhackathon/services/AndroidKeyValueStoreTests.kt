package com.omainegra.vanhackathon.services

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import org.hamcrest.Matchers.`is`

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class AndroidKeyValueStoreTests {
    @Test
    fun putString_should_add_a_value_of_type_String_to_the_key_store() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        androidKeyValueStore.putString("SOME_KEY1", "SOME_VALUE")

        assertEquals("SOME_VALUE", sharedPreferences.getString("SOME_KEY1", ""))
    }

    @Test
    fun getString_should_return_an_Optional_containing_the_value_of_String_from_the_key_store_if_present() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        sharedPreferences.edit().putString("SOME_KEY2", "SOME_OTHER_VALUE").commit()
        val result = androidKeyValueStore.getString("SOME_KEY2")

        assertThat(result.nonEmpty(), `is`(true))
        assertThat(result.orNull()!!, `is`("SOME_OTHER_VALUE"))
    }

    @Test
    fun getString_should_return_an_Optional_containing_None_from_the_key_store_if_the_value_is_not_a_String() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        sharedPreferences.edit().putInt("SOME_KEY3", 1).commit()
        val result = androidKeyValueStore.getString("SOME_KEY3")

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun getString_should_return_an_Optional_containing_None_from_the_key_store_if_absent() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        val result = androidKeyValueStore.getString("SOME_KEY4")

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun putBoolean_should_add_a_value_of_type_Boolean_to_the_key_store() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        androidKeyValueStore.putBoolean("SOME_KEY5", true)

        assertEquals(true, sharedPreferences.getBoolean("SOME_KEY5", false))
    }

    @Test
    fun getBoolean_should_return_an_Optional_containing_the_value_of_Boolean_from_the_key_store_if_present() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        sharedPreferences.edit().putBoolean("SOME_KEY6", true).commit()
        val result = androidKeyValueStore.getBoolean("SOME_KEY6")

        assertThat(result.nonEmpty(), `is`(true))
        assertThat(result.orNull()!!, `is`(true))
    }

    @Test
    fun getBoolean_should_return_an_Optional_containing_None_from_the_key_store_if_the_value_is_not_a_Boolean() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        sharedPreferences.edit().putInt("SOME_KEY7", 1).commit()
        val result = androidKeyValueStore.getString("SOME_KEY7")

        assertThat(result.isEmpty(), `is`(true))
    }

    @Test
    fun getBoolean_should_return_an_Optional_containing_None_from_the_key_store_if_absent() {
        val appContext = InstrumentationRegistry.getTargetContext()
        val sharedPreferences = appContext.getSharedPreferences("KantinaKeyValueStore", Context.MODE_PRIVATE)
        val androidKeyValueStore = AndroidKeyValueStore(sharedPreferences)

        val result = androidKeyValueStore.getString("SOME_KEY8")

        assertThat(result.isEmpty(), `is`(true))
    }
}
