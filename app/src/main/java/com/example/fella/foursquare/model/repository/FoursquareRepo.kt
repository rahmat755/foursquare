package com.example.fella.foursquare.model.repository

import android.util.Log
import com.example.fella.foursquare.db.VenueDao
import com.example.fella.foursquare.model.FoursquareApi
import com.example.fella.foursquare.db.VenueItem
import com.example.fella.foursquare.util.ActivityScope
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton


class FoursquareRepo @Inject constructor(val api: FoursquareApi, val dao: VenueDao) {
    fun getVenuesFromApi(lat: String, lng: String): Observable<ArrayList<VenueItem>> {
        return api.getVenues("$lat,$lng")
                .flatMapSingle { list ->
                    Observable.fromIterable(list.response?.venues)
                            .map {
                                val icon: String? = if (it.categories!!.isNotEmpty()) {
                                    it.categories?.first()?.icon?.prefix + "bg_64" +
                                            it.categories?.first()?.icon?.suffix
                                } else null
                                VenueItem(id = it.id!!,
                                        name = it.name!!,
                                        address = it.location?.address ?: "",
                                        lat = it.location?.lat!!,
                                        lng = it.location?.lng!!,
                                        icon = icon,
                                        photos = null,
                                        bestPhoto = null,
                                        tips = null
                                )
                            }.toList()
                            .map {
                                val observableVenueList: ArrayList<VenueItem> = arrayListOf()
                                for (venue in it) {
                                    observableVenueList.add(venue)
                                }

                                return@map observableVenueList
                            }
                }.doOnNext {
                    Log.d("ITEMzz", it.first().name)
                    storeVenuesInDb(it)
                }
    }

    fun getVenueDetail(venueId: String): Observable<VenueItem> {
        return Observable.concatArray(
                getVenueDetailFromDb(venueId),
                getVenueDetailFromApi(venueId)
        )
    }

    fun getVenues(lat: String, lng: String): Observable<List<VenueItem>> {
        return Observable.concatArray(
                getVenuesFromDb(),
                getVenuesFromApi(lat, lng))
    }


    fun getVenuesFromDb(): Observable<List<VenueItem>> {
        return dao.getVenues().filter { it.isNotEmpty() }
                .toObservable()
    }

    fun getVenueDetailFromDb(id: String): Observable<VenueItem> {
        return dao.getVenueDetail(id)
                .toObservable()
    }

    fun deleteCache() {
        return dao.delete()
    }

    private fun getVenueDetailFromApi(id: String): Observable<VenueItem> {
        return api.getVenueDetail(id)
                .map {
                    it.response?.venue
                }.map { ve ->
                    val mPhotos: ArrayList<String> = arrayListOf()
                    ve.photos?.groups?.forEach { group ->
                        group.items?.forEach {
                            mPhotos.add(it.prefix + it.width + "x" + it.height + it.suffix)
                        }
                    }
                    val mBestPhoto =
                            if (ve.bestPhoto != null) {
                                ve.bestPhoto!!.prefix +
                                        ve.bestPhoto!!.width + "x" + ve.bestPhoto!!.height +
                                        ve.bestPhoto!!.suffix
                            } else
                                ve.categories?.first()?.icon?.prefix + "bg_64" + ve.categories?.first()?.icon?.suffix

                    val icon: String? = if (ve.categories!!.isNotEmpty()) {
                        ve.categories?.first()?.icon?.prefix + "bg_64" +
                                ve.categories?.first()?.icon?.suffix
                    } else null
                    VenueItem(id = ve.id!!,
                            name = ve.name!!,
                            address = ve.location?.address ?: "",
                            lat = ve.location?.lat!!,
                            lng = ve.location?.lng!!,
                            icon = icon ?: "",
                            photos = mPhotos,
                            bestPhoto = mBestPhoto,
                            tips = ve.stats?.tipCount.toString()
                    )

                }
    }

    fun storeVenuesInDb(users: ArrayList<VenueItem>) {
        Observable.fromCallable { dao.insertAll(users) }
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe {
                    Log.d("dao", users.first().name)
                }
    }

}