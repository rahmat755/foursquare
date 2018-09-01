package com.example.fella.foursquare.db

import android.arch.persistence.room.*
import io.reactivex.Single

@Dao

interface VenueDao {

    @Query("SELECT * FROM venues")
    fun getVenues(): Single<List<VenueItem>>

    @Query("SELECT * FROM venues WHERE id = :id")
    fun getVenueDetail(id: String): Single<VenueItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(venue: VenueItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(venues: List<VenueItem>)

    @Query("DELETE FROM venues")
    fun delete()
}