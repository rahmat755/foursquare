package com.example.fella.foursquare.model.entities.venue_detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Photos {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("groups")
    @Expose
    var groups: List<Group>? = null
    @SerializedName("summary")
    @Expose
    var summary: String? = null
    @SerializedName("items")
    @Expose
    private val items: List<Any>? = null
}
