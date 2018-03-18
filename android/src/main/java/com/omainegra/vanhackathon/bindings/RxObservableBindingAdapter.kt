package com.omainegra.vanhackathon.bindings

import android.view.View
import io.reactivex.disposables.Disposable

/**
 * Created by omainegra on 3/18/18.
 */

open class RxObservableBindingAdapter {
    val disposables: MutableMap<View, Disposable> = HashMap()


    fun putDisposable(view: View, disposable: Disposable) {
        if (disposables.containsKey(view)) {
            disposables[view]?.dispose()
            disposables.remove(view)
        }

        disposables[view] = disposable

        view.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(ignored: View) {}

            override fun onViewDetachedFromWindow(ignored: View) {
                disposables[view]?.dispose()
            }
        })
    }
}