package com.example.fella.foursquare.model

import com.example.fella.foursquare.model.entities.venue.LocResponse
import com.example.fella.foursquare.model.entities.venue_detail.VenueDetailResponce
import com.example.fella.foursquare.util.CLIENT_ID
import com.example.fella.foursquare.util.CLIENT_SECRET
import com.example.fella.foursquare.util.V
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FoursquareApi {
    @GET("venues/search")
    fun getVenues(@Query("ll") latLng: String,
                  @Query("client_secret") apiKey: String = CLIENT_SECRET,
                  @Query("client_id") clientId: String = CLIENT_ID,
                  @Query("v") version: String = V): Observable<LocResponse>

    @GET("/v2/venues/{venue_id}")
    fun getVenueDetail(@Path("venue_id") venueID: String,
                       @Query("v") version: String = V,
                       @Query("client_id") clientID: String = CLIENT_ID,
                       @Query("client_secret") clientSecret: String = CLIENT_SECRET): Observable<VenueDetailResponce>
}