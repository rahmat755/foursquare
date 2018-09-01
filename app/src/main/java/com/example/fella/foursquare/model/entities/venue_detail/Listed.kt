package com.example.fella.foursquare.model.entities.venue_detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Listed {

    @SerializedName("count")
    @Expose
    var count: Int? = null
    @SerializedName("groups")
    @Expose
    var groups: List<Group__>? = null

}
