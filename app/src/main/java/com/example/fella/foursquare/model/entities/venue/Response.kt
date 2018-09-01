package com.example.fella.foursquare.model.entities.venue

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Response {

    @SerializedName("venues")
    @Expose
    var venues: ArrayList<Venue>? = null
    @SerializedName("confident")
    @Expose
    var confident: Boolean? = null

}
