package com.omainegra.vanhackathon.controllers

import com.omainegra.vanhackathon.injector
import com.omainegra.vanhackathon.view.SplashNavigation
import com.omainegra.vanhackathon.view.SplashViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.robovm.apple.uikit.UIStoryboard
import org.robovm.apple.uikit.UIViewController
import org.robovm.objc.annotation.CustomClass
import org.slf4j.LoggerFactory
import javax.inject.Inject

@CustomClass("SplashViewController")
class SplashViewController : UIViewController() {

    val log = LoggerFactory.getLogger(javaClass)

    @Inject lateinit var viewModel: SplashViewModel

    private val disposable = CompositeDisposable()

    override fun viewDidLoad() {
        super.viewDidLoad()
        injector.inject(this)
        viewModel.start()

        viewModel.navigation()
                .map {
                    val storyBoard = UIStoryboard("Main", null)
                    when (it){
                        is SplashNavigation.Home -> {
                            storyBoard.instantiateViewController("HomeViewController")
                        }
                        is SplashNavigation.Register -> {
                            //TODO : Just for testing
                            storyBoard.instantiateViewController("HomeViewController")
                        }
                    }
                }
                .subscribe { presentViewController(it, true, null) }
                .addTo(disposable)
    }

    override fun viewWillDisappear(p0: Boolean) {
        disposable.dispose()
        viewModel.stop()
    }
}