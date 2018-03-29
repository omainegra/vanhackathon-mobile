package com.omainegra.vanhackathon.view

import arrow.core.*
import arrow.syntax.applicative.tupled
import arrow.syntax.functor.map
import com.omainegra.vanhackathon.model.NewCustomer
import com.omainegra.vanhackathon.services.*
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.withLatestFrom
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit.MILLISECONDS
/**
 * Created by omainegra on 2/21/18.
 */

sealed class RegisterNavigation : Navigation {
    object Home : RegisterNavigation()
    object SignIn : RegisterNavigation()
}

interface RegisterViewModel : ViewModel<RegisterNavigation> {
    fun nameChanged(): Observer<String>
    fun emailChanged(): Observer<String>
    fun addressChanged(): Observer<String>
    fun passwordChanged(): Observer<String>
    fun registerClicked(): Observer<Unit>
    fun signInClicked(): Observer<Unit>

    fun nameErrors(): Observable<Option<String>>
    fun addressErrors(): Observable<Option<String>>
    fun emailErrors(): Observable<Option<String>>
    fun passwordErrors(): Observable<Option<String>>
    fun serverErrors(): Observable<String>
    fun registerButtonEnabled(): Observable<Boolean>
}

class RegisterViewModelImpl(
    private val scheduler: Scheduler,
    private val security: Security): RegisterViewModel {

    private val log = LoggerFactory.getLogger(javaClass)

    private val disposable = CompositeDisposable()
    private val nameSubject = BehaviorSubject.create<String>()
    private val emailSubject = BehaviorSubject.create<String>()
    private val addressSubject = BehaviorSubject.create<String>()
    private val passwordSubject = BehaviorSubject.create<String>()
    private val registerClickedSubject = BehaviorSubject.create<Unit>()
    private val signInClickedSubject = BehaviorSubject.create<Unit>()

    private val nameErrorSubject = BehaviorSubject.create<Option<String>>()
    private val addressErrorSubject = BehaviorSubject.create<Option<String>>()
    private val emailErrorSubject = BehaviorSubject.create<Option<String>>()
    private val passwordErrorSubject = BehaviorSubject.create<Option<String>>()
    private val serverErrorSubject = PublishSubject.create<String>()
    private val registerButtonEnabledSubject = BehaviorSubject.create<Boolean>()

    private val navigationSubject = BehaviorSubject.create<RegisterNavigation>()

    override fun start() {
        log.info("RegisterViewModelImpl started")

        //Initial state
        registerButtonEnabledSubject.onNext(false)

        val nameValidation = nameSubject
            .debounce(100, MILLISECONDS, scheduler)
            .map(::validateEmpty)

        val addressValidation = addressSubject
            .debounce(100, MILLISECONDS, scheduler)
            .map(::validateEmpty)

        val emailValidation = emailSubject
            .debounce(100, MILLISECONDS, scheduler)
            .map(::validateEmpty)
            .map { it.flatMap(::validateEmail) }

        val passwordValidation: Observable<Either<Exception, String>> = passwordSubject
            .debounce(100, MILLISECONDS, scheduler)
            .map(::validateEmpty)
            .map { it.flatMap { validateLength(it, 8)} }

        // Get valid fields
        val registrations = Observables.combineLatest(
            nameValidation, addressValidation, emailValidation, passwordValidation ) { name, address, email, password ->
                Option.applicative()
                    .tupled(name.toOption(), address.toOption(), email.toOption(), password.toOption())
                    .map { NewCustomer(name = it.a, address = it.b, email = it.c, password = it.d) }
                    .ev()
            }
            .doOnNext { registerButtonEnabledSubject.onNext( it.fold({ false }, { true }) ) }
            .flatMap { it.fold({ Observable.empty<NewCustomer>() }, { Observable.just(it) }) }

        // Show validation errors
        nameValidation.map {
                it.swap().toOption().flatMap { Option.fromNullable(it.message) }
            }
            .subscribe(nameErrorSubject::onNext)
            .addTo(disposable)

        addressValidation.map {
                it.swap().toOption().flatMap { Option.fromNullable(it.message) }
            }
            .subscribe(addressErrorSubject::onNext)
            .addTo(disposable)

        emailValidation.map {
                it.swap().toOption().flatMap { Option.fromNullable(it.message) }
            }
            .subscribe(emailErrorSubject::onNext)
            .addTo(disposable)

        passwordValidation.map {
                it.swap().toOption().flatMap { Option.fromNullable(it.message) }
            }
            .subscribe(passwordErrorSubject::onNext)
            .addTo(disposable)

        // Trigger network request
        registerClickedSubject.withLatestFrom(registrations).map { it.second }
            .doOnNext { registerButtonEnabledSubject.onNext(false) }
            .switchMap(security::createCustomer)
            .doOnNext { registerButtonEnabledSubject.onNext(true) }
            .subscribe {
                it.fold({ error ->
                        log.error("Error Registering user", error)
                        serverErrorSubject.onNext(error.message)
                    }, {
                        log.info("Register successful")
                        navigationSubject.onNext(RegisterNavigation.Home)
                    })
            }
            .addTo(disposable)

    }

    override fun nameChanged(): Observer<String> = nameSubject
    override fun addressChanged(): Observer<String> = addressSubject
    override fun emailChanged(): Observer<String> = emailSubject
    override fun passwordChanged(): Observer<String> = passwordSubject
    override fun registerClicked(): Observer<Unit> = registerClickedSubject
    override fun signInClicked(): Observer<Unit> = signInClickedSubject
    override fun registerButtonEnabled(): Observable<Boolean> = registerButtonEnabledSubject

    override fun nameErrors(): Observable<Option<String>> = nameErrorSubject
    override fun addressErrors(): Observable<Option<String>> = addressErrorSubject
    override fun emailErrors(): Observable<Option<String>> = emailErrorSubject
    override fun passwordErrors(): Observable<Option<String>> = passwordErrorSubject
    override fun serverErrors(): Observable<String> = serverErrorSubject

    override fun navigation(): Observable<RegisterNavigation> = navigationSubject.observeOn(scheduler)

    override fun stop() {
        log.info("RegisterViewModelImpl stopped")
        disposable.dispose()
    }
}