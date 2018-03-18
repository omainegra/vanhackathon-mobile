package com.omainegra.vanhackathon.services

import android.content.SharedPreferences
import arrow.core.Option
import arrow.data.Try
import arrow.syntax.option.none


/**
 * Created by omainegra on 1/19/18.
 */

class AndroidKeyValueStore(private val sharedPreferences: SharedPreferences) : KeyValueStore {

    override fun putString(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun getString(key: String): Option<String> =
        Try.invoke { sharedPreferences.getString(key, null) }
            .toOption()
            .flatMap { Option.fromNullable(it) }

    override fun putBoolean(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    override fun getBoolean(key: String): Option<Boolean> =
        if (sharedPreferences.contains(key))
            Try.invoke { sharedPreferences.getBoolean(key, false) }
                .toOption()
                .flatMap { Option.fromNullable(it) }
        else none()
}
