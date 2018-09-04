package com.example.fella.foursquare.di.allvenues

import com.example.fella.foursquare.model.FoursquareApi
import com.example.fella.foursquare.model.repository.FoursquareRepo
import com.example.fella.foursquare.presenter.AllVenuesPresenter
import com.example.fella.foursquare.presenter.MVPContract
import com.example.fella.foursquare.util.ActivityScope
import com.example.fella.foursquare.util.BASE_URL
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class AllVenuesModule(val view: MVPContract.AllVenuesView) {
    @ActivityScope
    @Provides
    fun provideView() = view

    @ActivityScope
    @Provides
    fun providePresenter(repo: FoursquareRepo, viewAll: MVPContract.AllVenuesView) =
            AllVenuesPresenter(repo, viewAll)

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