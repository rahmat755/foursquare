package com.example.fella.foursquare.model.entities.venue_detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Inbox {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("items")
    @Expose
    var items: List<Any>? = null

}
