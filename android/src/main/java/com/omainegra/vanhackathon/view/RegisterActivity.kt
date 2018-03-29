package com.omainegra.vanhackathon.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.omainegra.vanhackathon.BuildConfig
import com.omainegra.vanhackathon.R
import com.omainegra.vanhackathon.databinding.ActivityRegisterBinding
import com.omainegra.vanhackathon.extensions.component
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.slf4j.LoggerFactory
import javax.inject.Inject

class RegisterActivity : AppCompatActivity(){

    private val log = LoggerFactory.getLogger(javaClass)

    @Inject lateinit var viewModel: RegisterViewModel

    private val disposable = CompositeDisposable()
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_register)
        binding.viewModel = viewModel

        // View -> ViewModel events
        binding.nameEditText.textChanges()
            .skip(1).map { it.toString() }
            .subscribe(viewModel.nameChanged()::onNext)
            .addTo(disposable)

        binding.adddressEditText.textChanges()
            .skip(1).map { it.toString() }
            .subscribe(viewModel.addressChanged()::onNext)
            .addTo(disposable)

        binding.emailEditText.textChanges()
            .skip(1).map { it.toString() }
            .subscribe(viewModel.emailChanged()::onNext)
            .addTo(disposable)

        binding.passwordEditText.textChanges()
            .skip(1).map { it.toString() }
            .subscribe(viewModel.passwordChanged()::onNext)
            .addTo(disposable)

        binding.registerButton.clicks()
            .subscribe(viewModel.registerClicked()::onNext)
            .addTo(disposable)

        binding.goSignInButton.clicks()
            .subscribe(viewModel.signInClicked()::onNext)
            .addTo(disposable)

        // ViewModel -> View notifications
        viewModel.serverErrors()
            .subscribe {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
            }
            .addTo(disposable)

        viewModel.navigation()
            .subscribe {
                val destination = when (it) {
                    RegisterNavigation.Home -> HomeActivity.newIntent(this)
                    RegisterNavigation.SignIn -> throw Exception("Still working on it")
                }

                startActivity(destination)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }
            .addTo(disposable)

        if (savedInstanceState == null) viewModel.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        if (isFinishing) viewModel.stop()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, RegisterActivity::class.java)
    }
}