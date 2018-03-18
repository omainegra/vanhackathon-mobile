package com.omainegra.vanhackathon.bindings

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputLayout
import android.widget.TextView
import arrow.core.Option
import io.reactivex.Observable

/**
 * Created by omainegra on 3/18/18.
 */


object TextViewBindingAdapters : RxObservableBindingAdapter() {

    @JvmStatic @BindingAdapter("android:enable")
    fun setEnable(textView: TextView, errors: Observable<Boolean>){
        putDisposable(textView, errors.subscribe {
            textView.isEnabled = it
        })
    }
}