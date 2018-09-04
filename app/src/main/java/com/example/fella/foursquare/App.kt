package com.example.fella.foursquare

import android.app.Application
import android.os.Parcelable
import com.example.fella.foursquare.di.app.AppComponent
import com.example.fella.foursquare.di.app.AppModule
import com.example.fella.foursquare.di.app.DaggerAppComponent
import com.facebook.drawee.backends.pipeline.Fresco


class App : Application() {


    override fun onCreate() {
        super.onCreate()
        appComponent= DaggerAppComponent.builder().appModule(AppModule(this)).build()
        Fresco.initialize(this)
    }

    companion object {
        lateinit var appComponent: AppComponent
        lateinit var state: Parcelable
    }
}