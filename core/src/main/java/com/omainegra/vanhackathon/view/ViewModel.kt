package com.omainegra.vanhackathon.view

import io.reactivex.Observable

/**
 * Created by omainegra on 3/6/18.
 */

interface Navigation

interface ViewModel<N : Navigation> {
    fun navigation(): Observable<N>
    fun start()
    fun stop()
}

sealed class LCE<out T>
object Loading : LCE<Nothing>()
data class Content<out T>(val data: T) : LCE<T>()
data class Error(val message: String) : LCE<Nothing>()
