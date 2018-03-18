package com.omainegra.vanhackathon.services

import arrow.core.Option
import arrow.data.getOption

/**
 * Created by omainegra on 1/19/18.
 */
interface KeyValueStore {
    fun putString(key: String, value: String)
    fun getString(key: String): Option<String>

    fun putBoolean(key: String, value: Boolean)
    fun getBoolean(key: String): Option<Boolean>
}

class InMemoryKeyValueStore(private val map: HashMap<String, Any>) : KeyValueStore {

    override fun putString(key: String, value: String) {
        map[key] = value
    }

    override fun getString(key: String): Option<String> =
            map.getOption(key)
                    .filter { it is String }
                    .map { it as String }

    override fun putBoolean(key: String, value: Boolean) {
        map[key] = value
    }

    override fun getBoolean(key: String): Option<Boolean> =
            map.getOption(key)
                    .filter { it is Boolean }
                    .map { it as Boolean }
}