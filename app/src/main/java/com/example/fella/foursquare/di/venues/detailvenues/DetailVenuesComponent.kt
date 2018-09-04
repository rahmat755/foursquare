package com.example.fella.foursquare.di.venues.detailvenues

import com.example.fella.foursquare.di.venues.DbModule
import com.example.fella.foursquare.di.app.AppComponent
import com.example.fella.foursquare.util.ActivityScope
import com.example.fella.foursquare.view.DetailActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [AppComponent::class], modules = [DetailVenueModule::class, DbModule::class])
interface DetailVenuesComponent {
    fun inject(activity: DetailActivity)
}