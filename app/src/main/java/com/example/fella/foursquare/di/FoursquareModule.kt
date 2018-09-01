package com.example.fella.foursquare.di

import android.arch.lifecycle.ViewModelProvider
import com.example.fella.foursquare.model.FoursquareApi
import com.example.fella.foursquare.model.repository.FoursquareRepo
import com.example.fella.foursquare.viewmodel.VenuesViewModelFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class FoursquareModule {
    @Provides
    @Singleton
    fun provideApi(retrofit: Retrofit): FoursquareApi =retrofit.create(FoursquareApi::class.java)

}