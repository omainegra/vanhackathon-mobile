package com.omainegra.vanhackathon.utils

import io.reactivex.Observable
import org.robovm.apple.foundation.NSObject
import org.robovm.apple.uikit.UITextField

class RxTextField(val textField: UITextField) : NSObject() {

    fun textChanges(): Observable<String> = Observable.create { source ->
        textField.addOnEditingChangedListener { control ->
            source.onNext((control as UITextField).text)
        }
    }
}

fun UITextField.textChanges() = RxTextField(this).textChanges()