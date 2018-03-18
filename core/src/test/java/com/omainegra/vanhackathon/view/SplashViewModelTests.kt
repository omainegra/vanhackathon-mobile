package com.omainegra.vanhackathon.view

import com.omainegra.vanhackathon.services.NoOpInitializer
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

/**
 * Created by omainegra on 2/22/18.
 */

class SplashViewModelTests : StringSpec({

    "navigation should send to Home after initialize the Application" {
        val scheduler = TestScheduler()
        val viewModel = SplashViewModelImpl(scheduler, NoOpInitializer())

        viewModel.start()

        val navigation = viewModel.navigation().test()
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        navigation.values() shouldBe listOf(SplashNavigation.Home)
    }

})