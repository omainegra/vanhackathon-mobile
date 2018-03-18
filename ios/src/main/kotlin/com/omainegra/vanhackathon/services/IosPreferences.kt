package com.omainegra.vanhackathon.services

import arrow.core.None
import arrow.core.Option
import arrow.core.Some
import org.robovm.apple.foundation.NSUserDefaults

/**
 * Created by omainegra on 2/9/18.
 */

class IosKeyValueStore : KeyValueStore {
    override fun putBoolean(key: String, value: Boolean) {
        NSUserDefaults.getStandardUserDefaults().put(key, value)
    }

    override fun getBoolean(key: String): Option<Boolean> =
        if (containsKey(key)) Some(NSUserDefaults.getStandardUserDefaults().getBoolean(key))
        else None

    override fun putString(key: String, value: String) {
        NSUserDefaults.getStandardUserDefaults().put(key, value)
    }

    override fun getString(key: String): Option<String> =
        Option.fromNullable(NSUserDefaults.getStandardUserDefaults().getString(key))

    private fun containsKey(key: String) = NSUserDefaults.getStandardUserDefaults().get(key) != null
}
