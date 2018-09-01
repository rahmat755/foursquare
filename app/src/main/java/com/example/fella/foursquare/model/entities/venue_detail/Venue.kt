package com.example.fella.foursquare.model.entities.venue_detail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Venue {

    @SerializedName("id")
    @Expose
    var id: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("contact")
    @Expose
    var contact: Contact? = null
    @SerializedName("location")
    @Expose
    var location: Location? = null
    @SerializedName("canonicalUrl")
    @Expose
    var canonicalUrl: String? = null
    @SerializedName("categories")
    @Expose
    var categories: List<Category>? = null
    @SerializedName("verified")
    @Expose
    var verified: Boolean? = null
    @SerializedName("stats")
    @Expose
    var stats: Stats? = null
    @SerializedName("likes")
    @Expose
    var likes: Likes? = null
    @SerializedName("dislike")
    @Expose
    var dislike: Boolean? = null
    @SerializedName("ok")
    @Expose
    var ok: Boolean? = null
    @SerializedName("beenHere")
    @Expose
    var beenHere: BeenHere? = null
    @SerializedName("specials")
    @Expose
    var specials: Specials? = null
    @SerializedName("photos")
    @Expose
    var photos: Photos? = null
    @SerializedName("reasons")
    @Expose
    var reasons: Reasons? = null
    @SerializedName("hereNow")
    @Expose
    var hereNow: HereNow? = null
    @SerializedName("createdAt")
    @Expose
    var createdAt: Int? = null
    @SerializedName("tips")
    @Expose
    var tips: Tips? = null
    @SerializedName("shortUrl")
    @Expose
    var shortUrl: String? = null
    @SerializedName("timeZone")
    @Expose
    var timeZone: String? = null
    @SerializedName("listed")
    @Expose
    var listed: Listed? = null
    @SerializedName("pageUpdates")
    @Expose
    var pageUpdates: PageUpdates? = null
    @SerializedName("inbox")
    @Expose
    var inbox: Inbox? = null
    @SerializedName("attributes")
    @Expose
    var attributes: Attributes? = null
    @SerializedName("bestPhoto")
    @Expose
    var bestPhoto: BestPhoto? = null

}
