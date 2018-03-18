package com.omainegra.vanhackathon.extensions

import android.support.v4.app.Fragment
import arrow.core.Option
import arrow.core.getOrElse

/**
 * Created by omainegra on 2/27/18.
 */

fun Fragment.getStringParam(key: String): String =
    Option.fromNullable(arguments)
        .map { it.getString(key) }
        .getOrElse { throw Exception("Arguments not set") }