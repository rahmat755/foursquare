package com.example.fella.foursquare.presenter

import com.example.fella.foursquare.model.repository.FoursquareRepo
import com.example.fella.foursquare.util.FROM_API
import com.example.fella.foursquare.util.FROM_DB
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import io.reactivex.disposables.Disposable
import io.reactivex.CompletableObserver
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable


class AllVenuesPresenter @Inject constructor(private val repo: FoursquareRepo, val view: MVPContract.AllVenuesView) : MVPContract.VenuesPresenter {
    private val compositeDisposable = CompositeDisposable()

    override fun destroy() {
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
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
                        view.displayError(e) { dropData() }
                    }
                })
    }

    override fun loadData(lat: String, lng: String, sourceType: Int) {
        compositeDisposable.add(
                when (sourceType) {
                    FROM_API -> repo.getVenues(lat, lng)
                            .subscribeOn(Schedulers.io())

                            .debounce(400, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                    {
                                        view.displayData(ArrayList(it))
                                        view.showProgressBar(false)
                                    }
                            ) {
                                view.displayError(it) { loadData(lat, lng, sourceType) }
                            }
                    FROM_DB ->
                        repo.getVenuesFromDb()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        {
                                            view.displayData(ArrayList(it))
                                            view.showProgressBar(false)
                                        }
                                ) {
                                    view.displayError(it) { loadData(lat, lng, sourceType) }
                                }

                    else -> {
                        return
                    }
                }
        )

    }

    override fun loadData(id: String, sourceType: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}