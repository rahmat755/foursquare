package com.example.fella.foursquare.db

import android.arch.persistence.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class Converters {
    @TypeConverter
    fun fromString(value: String?): ArrayList<String>? {
        val listType = object : TypeToken<ArrayList<String>>() {
        }.type
        return when (value) {
            null -> null
            else -> Gson().fromJson(value, listType)
        }
    }

    @TypeConverter
    fun fromArrayList( list: ArrayList<String>?): String? {
        val gson = Gson()
        return when (list) {
            null -> null
            else -> gson.toJson(list)
        }
    }
}