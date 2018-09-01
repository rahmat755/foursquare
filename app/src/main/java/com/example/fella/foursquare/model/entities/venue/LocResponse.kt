package com.example.fella.foursquare.model.entities.venue

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class LocResponse {

    @SerializedName("meta")
    @Expose
    var meta: Meta? = null
    @SerializedName("response")
    @Expose
    var response: Response? = null

}
