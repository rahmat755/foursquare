package com.example.fella.foursquare.di

import com.example.fella.foursquare.view.DetailActivity
import com.example.fella.foursquare.view.MainActivity
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [
    NetworkModule::class,
    FoursquareModule::class,
    AppModule::class])
interface FoursquareComponent {
    fun injectMainActivity(activity: MainActivity)
    fun injectDetailActivity(activity: DetailActivity)
}