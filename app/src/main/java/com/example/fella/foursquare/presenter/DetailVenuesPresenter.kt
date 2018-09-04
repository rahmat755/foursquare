package com.example.fella.foursquare.presenter

import com.example.fella.foursquare.model.repository.FoursquareRepo
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class DetailVenuesPresenter@Inject constructor(val repo: FoursquareRepo, val view: MVPContract.DetailVenuesView): MVPContract.VenuesPresenter {
    override fun loadData(lat: String, lng: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun loadData(id: String) {
        view.showProgressBar(true)
        repo.getVenueDetail(id)
                .subscribeOn(Schedulers.io())
                .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            view.displayData(it)
                            view.showProgressBar(false)
                        }
                ) {
                    view.displayError(it) {loadData(id)}
                }
    }

    override fun dropData() {
        Completable.fromAction {
            repo.deleteCache()
        }
                .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : CompletableObserver {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onComplete() {
                        view.showToast("Cleared!")
                    }

                    override fun onError(e: Throwable) {
                        view.displayError(e) {dropData()}
                    }
                })
    }
}