package com.omainegra.vanhackathon.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.omainegra.vanhackathon.extensions.component
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SplashActivity : AppCompatActivity(){

    private val disposable = CompositeDisposable()

    @Inject lateinit var  viewModel: SplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        viewModel.navigation()
            .subscribe {
                val destination = when (it) {
                    SplashNavigation.Register -> RegisterActivity.newIntent(this)
                    SplashNavigation.Home -> HomeActivity.newIntent(this)
                }

                startActivity(destination)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
            .addTo(disposable)

        viewModel.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        if (isFinishing) viewModel.stop()
    }
}
