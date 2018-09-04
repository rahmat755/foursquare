package com.example.fella.foursquare.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.location.Location
import android.util.Log
import com.example.fella.foursquare.db.VenueItem
import com.example.fella.foursquare.model.repository.FoursquareRepo
import com.example.fella.foursquare.util.Event
import io.reactivex.Observer
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class VenuesViewModel @Inject constructor(var repository: FoursquareRepo) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    var venuesList: MutableLiveData<ArrayList<VenueItem>>? = null
    var _location: MutableLiveData<Location> = MutableLiveData()
    var venueDetail: MutableLiveData<VenueItem>? = null
    private val _showError = MutableLiveData<Event<String>>()
    private val _showMessage = MutableLiveData<Event<String>>()
    private val _showNoDataError = MutableLiveData<Event<String>>()
    private val _showAllVenuesProgressBar = MutableLiveData<Boolean>()
    private val _showDetailVenueProgressBar = MutableLiveData<Boolean>()
    val _venueId = MutableLiveData<String>()

    val showError: LiveData<Event<String>>
        get() = _showError
    val showMessage: LiveData<Event<String>>
        get() = _showMessage

    val showNoDataError: LiveData<Event<String>>
        get() = _showNoDataError
    val location: LiveData<Location>
        get() = _location
    val showAllVenuesProgressBar: LiveData<Boolean>
        get() = _showAllVenuesProgressBar
    val showDetailVenuesProgressBar: LiveData<Boolean>
        get() = _showDetailVenueProgressBar

    fun getVenuesList(lat: String, lng: String): LiveData<ArrayList<VenueItem>> {
        return if (venuesList == null) {
            Log.d("LOAD VENUES", lat)
            venuesList = MutableLiveData()
            loadVenues(lat, lng)
            venuesList!!
        } else
            venuesList!!
    }

    fun getVenueDetails(id:String): LiveData<VenueItem> {
        return if (venueDetail == null) {
            venueDetail = MutableLiveData()
            loadVenueDetailInfo(id)
            venueDetail!!
        } else
            venueDetail!!
    }

    fun dropData() {
        Single.fromCallable {
            repository.deleteCache()
        }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun loadVenuesFromDb() {
        _showAllVenuesProgressBar.postValue(true)
        compositeDisposable.add(
                repository.getVenuesFromDb()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .timeout(10000, TimeUnit.MILLISECONDS)
                        .subscribe(
                                {
                                    venuesList?.postValue(ArrayList(it))
                                    _showAllVenuesProgressBar.postValue(false)
                                },
                                {
                                    _showNoDataError.postValue(Event(it.localizedMessage))
                                }
                        ))
    }

    fun loadVenueDetailFromDb(venueId: String) {
        _showDetailVenueProgressBar.postValue(true)
        compositeDisposable.add(
                repository.getVenueDetailFromDb(venueId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                                {
                                    venueDetail?.postValue(it)
                                    _showDetailVenueProgressBar.postValue(false)
                                },
                                {
                                    _showError.postValue(Event(it.localizedMessage))
                                }
                        ))
    }

    fun loadVenues(latitude: String, longitude: String) {
        _showAllVenuesProgressBar.postValue(true)
        compositeDisposable.add(
                repository.getVenues(latitude, longitude)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .subscribe(
                                {
                                    venuesList?.postValue(ArrayList(it))
                                    _showAllVenuesProgressBar.postValue(false)
                                },
                                {
                                    _showError.postValue(Event(it.localizedMessage))
                                }
                        ))
    }

    fun loadVenueDetailInfo(venueId: String) {
        _showDetailVenueProgressBar.postValue(true)
        compositeDisposable.add(
                repository.getVenueDetail(venueId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .debounce(400, TimeUnit.MILLISECONDS)
                        .subscribe(
                                {
                                    venueDetail?.postValue(it)
                                    _showDetailVenueProgressBar.postValue(false)
                                },
                                {
                                    _showError.postValue(Event(it.localizedMessage))
                                }
                        ))
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("VIEWMODEL", "cleared")
        if (!compositeDisposable.isDisposed)
            compositeDisposable.dispose()
    }
}