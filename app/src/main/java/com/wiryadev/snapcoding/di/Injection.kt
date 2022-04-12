package com.wiryadev.snapcoding.di

import android.content.Context
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig

object Injection {

    fun provideRepository(context: Context): SnapRepository {
        val apiService = SnapCodingApiConfig.getService()
        return SnapRepository(apiService)
    }
}