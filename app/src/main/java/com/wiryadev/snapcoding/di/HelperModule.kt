package com.wiryadev.snapcoding.di

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.wiryadev.snapcoding.common.location.LocationHelper
import com.wiryadev.snapcoding.common.location.LocationHelperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HelperModule {

    @Provides
    @Singleton
    fun providesLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun providesGeocoder(
        @ApplicationContext context: Context
    ): Geocoder = Geocoder(context)

    @Provides
    fun bindsLocationHelper(
        fusedLocationProviderClient: FusedLocationProviderClient,
        geocoder: Geocoder,
    ): LocationHelper = LocationHelperImpl(
        locationProviderClient = fusedLocationProviderClient,
        geocoder = geocoder,
    )

}