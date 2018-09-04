package com.example.fella.foursquare.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey


@Entity(tableName = "venues")
data class VenueItem(
        @PrimaryKey
        @ColumnInfo(name = "id")
        var id: String,
        @ColumnInfo(name = "name")
        var name: String,
        @ColumnInfo(name = "address")
        var address: String,
        @ColumnInfo(name = "lat")
        var lat: Double,
        @ColumnInfo(name = "lng")
        var lng: Double,
        @ColumnInfo(name = "photos")
        var photos: ArrayList<String>?,
        @ColumnInfo(name = "bestPhoto")
        var bestPhoto: String?,
        @ColumnInfo(name = "icon")
        var icon: String,
        @ColumnInfo(name = "tips")
        var tips: String?
)