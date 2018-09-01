package com.example.fella.foursquare

import android.app.Application
import android.os.Parcelable
import com.example.fella.foursquare.di.AppModule
import com.example.fella.foursquare.di.DaggerFoursquareComponent
import com.example.fella.foursquare.di.FoursquareComponent
import com.facebook.drawee.backends.pipeline.Fresco


class App: Application() {
    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerFoursquareComponent.builder().appModule(AppModule(this)).build()
        Fresco.initialize(this)
    }
    companion object {
        lateinit var appComponent: FoursquareComponent
        lateinit var state: Parcelable
    }
}