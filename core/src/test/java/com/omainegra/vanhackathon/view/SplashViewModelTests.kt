package com.omainegra.vanhackathon.view

import com.omainegra.vanhackathon.services.InMemoryKeyValueStore
import com.omainegra.vanhackathon.services.NoOpInitializer
import com.omainegra.vanhackathon.services.PreferencesImpl
import io.kotlintest.matchers.shouldBe
import io.kotlintest.specs.StringSpec
import io.reactivex.schedulers.TestScheduler
import java.util.concurrent.TimeUnit

/**
 * Created by omainegra on 2/22/18.
 */

class SplashViewModelTests : StringSpec({

    "navigation should send to Register if there is no access token in preferences" {
        val scheduler = TestScheduler()
        val map: HashMap<String, Any> = HashMap()
        val preferences = PreferencesImpl(InMemoryKeyValueStore(map), scheduler)
        val viewModel = SplashViewModelImpl(scheduler, NoOpInitializer(), preferences)

        viewModel.start()

        val navigation = viewModel.navigation().test()
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        navigation.values() shouldBe listOf(SplashNavigation.Register)
    }

    "navigation should send to Home if there is an access token in preferences" {
        val scheduler = TestScheduler()
        val map: HashMap<String, Any> = HashMap()
        map["ACCESS_TOKEN_PARAM"] = "access_token"

        val preferences = PreferencesImpl(InMemoryKeyValueStore(map), scheduler)
        val viewModel = SplashViewModelImpl(scheduler, NoOpInitializer(), preferences)

        viewModel.start()

        val navigation = viewModel.navigation().test()
        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        navigation.values() shouldBe listOf(SplashNavigation.Home)
    }

})