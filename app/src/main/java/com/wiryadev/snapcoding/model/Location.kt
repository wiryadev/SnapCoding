package com.wiryadev.snapcoding.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val lat: Double,
    val lon: Double,
) : Parcelable

fun Location.asLatLng() = LatLng(lat, lon,)