package com.example.fella.foursquare.di.allvenues

import android.app.Application
import android.arch.persistence.room.Room
import com.example.fella.foursquare.db.AppDatabase
import com.example.fella.foursquare.db.VenueDao
import com.example.fella.foursquare.util.ActivityScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DbModule( mApplication: Application) {

    private val appDatabase: AppDatabase = Room.databaseBuilder(mApplication,
            AppDatabase::class.java, "venue-database")
            .fallbackToDestructiveMigration().build()

    @ActivityScope
    @Provides
    internal fun providesRoomDatabase(): AppDatabase {
        return appDatabase
    }

    @ActivityScope
    @Provides
    internal fun providesProductDao(appDatabase: AppDatabase): VenueDao {
        return appDatabase.venuesDao()
    }

}