package com.example.fella.foursquare.model.entities.venue_detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Stats {

    @SerializedName("tipCount")
    @Expose
    var tipCount: Int? = null

}
