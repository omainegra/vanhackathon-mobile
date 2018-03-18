package com.omainegra.vanhackathon.utils

import io.reactivex.Observable
import org.robovm.apple.foundation.NSObject
import org.robovm.apple.uikit.UIButton


class RxButton(val button: UIButton) : NSObject() {

    fun clicks(): Observable<Unit> = Observable.create { source ->
        button.addOnTouchDownListener { _, _ ->
            source.onNext(Unit)
        }
    }
}

fun UIButton.clicks() = RxButton(this).clicks()