package com.omainegra.vanhackathon.controllers

import com.omainegra.vanhackathon.injector
import com.omainegra.robopods.sdwebimage.UIImageViewExtensions
import com.omainegra.vanhackathon.model.Store
import com.omainegra.vanhackathon.view.Content
import com.omainegra.vanhackathon.view.HomeViewModel
import com.omainegra.vanhackathon.view.Loading
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import org.robovm.apple.foundation.NSIndexPath
import org.robovm.apple.foundation.NSURL
import org.robovm.apple.uikit.*
import org.robovm.objc.annotation.CustomClass
import org.robovm.objc.annotation.IBOutlet
import org.slf4j.LoggerFactory
import javax.inject.Inject

@CustomClass("HomeViewController")
class HomeViewController : UIViewController() {

    private val log = LoggerFactory.getLogger(javaClass)
    private val disposable = CompositeDisposable()

    @Inject lateinit var viewModel: HomeViewModel

    @IBOutlet lateinit var tableView: UITableView
    @IBOutlet lateinit var loadingView: UIActivityIndicatorView

    override fun viewDidLoad() {
        super.viewDidLoad()
        injector.inject(this)

        val refreshControl = UIRefreshControl()
        refreshControl.addOnValueChangedListener {
            viewModel.pullToRefresh().onNext(Unit)
            refreshControl.endRefreshing()
        }

        tableView.refreshControl = refreshControl

        viewModel.start()
        viewModel.storesLce()
            .subscribe {
                when (it){
                    is Loading -> {
                        loadingView.startAnimating()
                    }
                    is Content -> {
                        tableView.dataSource = StoreTableViewDataSource(it.data)
//                        tableView.setDelegate(CharacterDelegate(viewModel))
                        tableView.reloadData()
                        loadingView.stopAnimating()
                    }
                    is Error -> {
                        log.info("Error: ${it.message}")
                        loadingView.stopAnimating()
                    }
                }

            }
            .addTo(disposable)
    }

    override fun viewWillDisappear(p0: Boolean) {
        disposable.dispose()
        viewModel.stop()
    }
}


class StoreTableViewDataSource(private val stores: List<Store>): UITableViewDataSourceAdapter() {

    val log = LoggerFactory.getLogger(javaClass)

    override fun getNumberOfRowsInSection(tableView: UITableView, section: Long) = stores.count().toLong()

    override fun getCellForRow(tableView: UITableView, indexPath: NSIndexPath): UITableViewCell {
        val store = stores[indexPath.row]
        val imageUrl: String = store.imageUrl

        log.info("getCellForRow: $store")

        val cell = tableView.dequeueReusableCell("storeViewCellIdentifier") as StoreViewCell
        cell.nameLabel.text = store.name
        cell.descriptionLabel.text = store.address
//        UIImageViewExtensions.setImage(cell.backgroundImage, NSURL("https://picsum.photos/1280/720/?image=${store.id}"), UIImage.getImage("im_not_found"))
        UIImageViewExtensions.setImage(cell.backgroundImage, NSURL(store.imageUrl), UIImage.getImage("im_not_found"))


        return cell
    }
}

@CustomClass("StoreViewCell")
class StoreViewCell : UITableViewCell() {
    @IBOutlet lateinit var nameLabel: UILabel
    @IBOutlet lateinit var descriptionLabel: UILabel
    @IBOutlet lateinit var backgroundImage: UIImageView
}