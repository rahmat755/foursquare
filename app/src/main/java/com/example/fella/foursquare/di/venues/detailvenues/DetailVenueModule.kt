package com.example.fella.foursquare.di.venues.detailvenues

import com.example.fella.foursquare.model.FoursquareApi
import com.example.fella.foursquare.model.repository.FoursquareRepo
import com.example.fella.foursquare.presenter.DetailVenuesPresenter
import com.example.fella.foursquare.presenter.MVPContract
import com.example.fella.foursquare.util.ActivityScope
import com.example.fella.foursquare.util.BASE_URL
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@Module
class DetailVenueModule(val view: MVPContract.DetailVenuesView) {
    @ActivityScope
    @Provides
    fun provideView() = view

    @ActivityScope
    @Provides
    fun providePresenter(repo: FoursquareRepo, viewAll: MVPContract.DetailVenuesView) =
            DetailVenuesPresenter(repo, viewAll)

    @Provides
    @ActivityScope
    fun provideFoursquareService(): FoursquareApi {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(FoursquareApi::class.java)
    }
}