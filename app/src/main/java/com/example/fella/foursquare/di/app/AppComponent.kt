package com.example.fella.foursquare.di.app

import com.example.fella.foursquare.App
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: App)
}