package com.omainegra.vanhackathon.bindings

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputLayout
import arrow.core.Option
import io.reactivex.Observable

/**
 * Created by omainegra on 3/18/18.
 */


object TextInputLayoutBindingAdapters : RxObservableBindingAdapter() {

    @JvmStatic @BindingAdapter("app:error")
    fun setErrors(textInputLayout: TextInputLayout, errors: Observable<Option<String>>){
        putDisposable(textInputLayout, errors.subscribe {
            it.fold(
                { textInputLayout.isErrorEnabled = false },
                { textInputLayout.error = it }
            )
        })
    }
}