package com.omainegra.vanhackathon.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import com.omainegra.vanhackathon.R
import com.omainegra.vanhackathon.databinding.ActivityRegisterBinding
import com.omainegra.vanhackathon.databinding.ActivitySigninBinding
import com.omainegra.vanhackathon.extensions.component
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class SignInActivity : AppCompatActivity(){

    @Inject lateinit var viewModel: RegisterViewModel

    private val disposable = CompositeDisposable()
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signin)

        // View -> ViewModel events
        binding.emailEditText.textChanges()
            .skip(1).map { it.toString() }
            .subscribe(viewModel.emailChanged()::onNext)
            .addTo(disposable)

        binding.passwordEditText.textChanges()
            .skip(1).map { it.toString() }
            .subscribe(viewModel.passwordChanged()::onNext)
            .addTo(disposable)

        binding.signInButton.clicks()
            .subscribe(viewModel.signInClicked()::onNext)
            .addTo(disposable)

        binding.goRegisterButton.clicks()
            .subscribe(viewModel.registerClicked()::onNext)
            .addTo(disposable)
//

        // ViewModel -> View notifications
        viewModel.emailErrors().subscribe {
                it.fold(
                    { binding.emailTextInputLayout.isErrorEnabled = false },
                    { binding.emailTextInputLayout.error = it })
            }
            .addTo(disposable)

        viewModel.passwordErrors().subscribe {
                it.fold(
                    { binding.passwordTextInputLayout.isErrorEnabled = false },
                    { binding.passwordTextInputLayout.error = it })
            }
            .addTo(disposable)

        viewModel.navigation()
            .subscribe {
                val destination = when (it) {
                    RegisterNavigation.Home -> HomeActivity.newIntent(this)
                    RegisterNavigation.SignIn -> HomeActivity.newIntent(this)
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
        fun newIntent(context: Context) = Intent(context, SignInActivity::class.java)
    }
}