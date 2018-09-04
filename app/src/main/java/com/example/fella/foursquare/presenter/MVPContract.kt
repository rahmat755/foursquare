package com.example.fella.foursquare.presenter

import com.example.fella.foursquare.db.VenueItem

interface MVPContract {
    interface AllVenuesView {
        fun displayData(venueList: ArrayList<VenueItem>)
        fun displayError(e: Throwable, func: () -> Unit)
        fun displayNoDataError()
        fun showToast(msg: String)
        fun showProgressBar(flag: Boolean)
    }
    interface DetailVenuesView{
        fun displayData(venue: VenueItem)
        fun displayError(e: Throwable, func: () -> Unit)
        fun showToast(msg: String)
        fun showProgressBar(flag: Boolean)
    }

    interface VenuesPresenter {
        fun loadData(lat: String, lng: String)
        fun loadData(id: String)
        fun dropData()
    }

}