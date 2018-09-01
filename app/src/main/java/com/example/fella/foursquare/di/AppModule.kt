package com.example.fella.foursquare.di

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.room.Room
import android.content.Context
import com.example.fella.foursquare.App
import com.example.fella.foursquare.db.AppDatabase
import com.example.fella.foursquare.db.VenueDao
import com.example.fella.foursquare.viewmodel.VenuesViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: App) {
    @Provides
    @Singleton
    fun provideContext(): Context = app.applicationContext

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    fun provideViewModelFactory(
            factory: VenuesViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    fun provideAppDatabase(context: Context): AppDatabase =
            Room.databaseBuilder(context,
                    AppDatabase::class.java, "venue-database")
                    .fallbackToDestructiveMigration().build()

    @Provides
    fun provideVenueDao(appDatabase: AppDatabase): VenueDao =
            appDatabase.userDao()
}