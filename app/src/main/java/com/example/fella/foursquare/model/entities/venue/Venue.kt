package com.example.fella.foursquare.model.entities.venue

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Venue {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null
    @SerializedName("referralId")
    @Expose
    var referralId: String? = null
    @SerializedName("hasPerk")
    @Expose
    var hasPerk: Boolean? = null

}
