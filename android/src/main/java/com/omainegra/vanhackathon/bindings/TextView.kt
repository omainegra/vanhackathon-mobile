package com.omainegra.vanhackathon.bindings

import android.databinding.BindingAdapter
import android.widget.Button
import android.widget.TextView
import io.reactivex.Observable

/**
 * Created by omainegra on 3/18/18.
 */


object TextViewBindingAdapters : RxObservableBindingAdapter() {

    @JvmStatic @BindingAdapter("android:enabled")
    fun setEnable(button: TextView, observable: Observable<Boolean>){
        putDisposable(button, observable.subscribe {
            button.isEnabled = it
        })
    }
}