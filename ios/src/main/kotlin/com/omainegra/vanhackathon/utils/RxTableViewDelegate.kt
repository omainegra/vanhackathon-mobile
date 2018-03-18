package com.omainegra.vanhackathon.utils

import io.reactivex.Observable
import org.robovm.apple.foundation.NSIndexPath
import org.robovm.apple.foundation.NSObject
import org.robovm.apple.uikit.UITableView
import org.robovm.apple.uikit.UITableViewCell
import org.robovm.apple.uikit.UITableViewDelegateAdapter


class RxTableViewDelegate(val tableView: UITableView) : NSObject() {

    fun willDisplayCell(): Observable<WillDisplayCellResult> = Observable.create { source ->
        tableView.setDelegate(object : UITableViewDelegateAdapter(){
            override fun willDisplayCell(tableView: UITableView, cell: UITableViewCell, indexPath: NSIndexPath) {
                super.willDisplayCell(tableView, cell, indexPath)
                source.onNext(WillDisplayCellResult(tableView, cell, indexPath))
            }
        })
    }
}

data class WillDisplayCellResult(val tableView: UITableView, val cell: UITableViewCell, val indexPath: NSIndexPath)

fun UITableView.willDisplayCell() = RxTableViewDelegate(this).willDisplayCell()