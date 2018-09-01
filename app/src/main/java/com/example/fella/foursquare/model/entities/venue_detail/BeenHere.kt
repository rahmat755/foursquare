package com.example.fella.foursquare.model.entities.venue_detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class BeenHere {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("unconfirmedCount")
    @Expose
    var unconfirmedCount: Int? = null
    @SerializedName("marked")
    @Expose
    var marked: Boolean? = null
    @SerializedName("lastCheckinExpiredAt")
    @Expose
    var lastCheckinExpiredAt: Int? = null

}
