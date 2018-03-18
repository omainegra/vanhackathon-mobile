package com.omainegra.vanhackathon.extensions

import android.app.Activity
import android.support.v7.app.AppCompatActivity
import com.omainegra.vanhackathon.AndroidApp
import com.omainegra.vanhackathon.dagger.ActivityComponent

/**
 * Created by omainegra on 2/23/18.
 */

val Activity.component: ActivityComponent
    get() = (this.application as AndroidApp).activityScopeManager.getComponent(this)!!