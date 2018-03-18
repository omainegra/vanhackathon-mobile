package com.omainegra.vanhackathon.view

import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.jakewharton.rxbinding2.support.v4.widget.refreshes
import com.omainegra.vanhackathon.R
import com.omainegra.vanhackathon.databinding.ActivityHomeBinding
import com.omainegra.vanhackathon.databinding.RowStoreBinding
import com.omainegra.vanhackathon.extensions.component
import com.omainegra.vanhackathon.model.Store
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
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

        val layoutManager = LinearLayoutManager(this)
        val adapter = StoresAdapter()

        binding.storesRecyclerView.layoutManager = layoutManager
        binding.storesRecyclerView.adapter = adapter

        viewModel.storesLce()
                .doOnNext(::println)
                .subscribe {
                    when (it) {
                        is Loading -> this.runOnUiThread {
                            binding.swipeRefreshLayout.isRefreshing = true
                        }
                        is Content -> {
                            adapter.updateData(it.data)
                            binding.swipeRefreshLayout.isRefreshing = false
                        }
                        is Error -> {
                            binding.swipeRefreshLayout.isRefreshing = false
                            Snackbar.make(binding.root, it.message, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
                .addTo(disposable)

        binding.swipeRefreshLayout.refreshes()
                .subscribe(viewModel.pullToRefresh()::onNext)
                .addTo(disposable)

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

class StoresAdapter : RecyclerView.Adapter<StoreViewHolder>() {

    var data: List<Store> = emptyList()

    fun updateData(data: List<Store>){
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewHolder =
        StoreViewHolder(RowStoreBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = data.count()

    override fun onBindViewHolder(holder: StoreViewHolder, position: Int) {
        holder.update(data[position])
    }

}

class StoreViewHolder(private val binding: RowStoreBinding) : RecyclerView.ViewHolder(binding.root) {
    fun update(item: Store){
        binding.value = item
    }
}