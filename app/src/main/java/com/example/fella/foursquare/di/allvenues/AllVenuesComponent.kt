package com.example.fella.foursquare.di.allvenues

import com.example.fella.foursquare.di.app.AppComponent
import com.example.fella.foursquare.util.ActivityScope
import com.example.fella.foursquare.view.MainActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [AllVenuesModule::class, DbModule::class])
interface AllVenuesComponent {
    fun inject(activity: MainActivity)
}