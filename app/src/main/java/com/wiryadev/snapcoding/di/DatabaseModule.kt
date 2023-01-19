package com.wiryadev.snapcoding.di

import android.content.Context
import androidx.room.Room
import com.wiryadev.snapcoding.BuildConfig
import com.wiryadev.snapcoding.data.local.room.RemoteKeysDao
import com.wiryadev.snapcoding.data.local.room.SnapDao
import com.wiryadev.snapcoding.data.local.room.SnapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesDatabase(
        @ApplicationContext context: Context,
    ): SnapDatabase = Room
        .databaseBuilder(
            context,
            SnapDatabase::class.java,
            BuildConfig.DATABASE_NAME,
        )
        .build()

    @Provides
    @Singleton
    fun providesSnapDao(
        database: SnapDatabase,
    ): SnapDao = database.snapDao()

    @Provides
    @Singleton
    fun providesRemoteKeysDao(
        database: SnapDatabase,
    ): RemoteKeysDao = database.remoteKeysDao()


}