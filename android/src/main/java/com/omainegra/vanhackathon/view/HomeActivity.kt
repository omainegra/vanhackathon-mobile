package com.omainegra.vanhackathon.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.omainegra.vanhackathon.R
import com.omainegra.vanhackathon.databinding.ActivityHomeBinding
import com.omainegra.vanhackathon.extensions.component
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class HomeActivity : AppCompatActivity(){

    @Inject lateinit var viewModel: HomeViewModel

    private val disposable = CompositeDisposable()
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        setSupportActionBar(binding.toolbar)

        viewModel.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        if (isFinishing) viewModel.stop()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }
}