package com.wiryadev.snapcoding.common.location

import android.location.Address
import android.location.Geocoder
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.model.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class LocationHelperImpl @Inject constructor(
    private val locationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
) : LocationHelper {

    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    override fun getLastLocation(): Flow<Result<Location>> = flow {
        try {
            val lastLocation = locationProviderClient.lastLocation.await()
            val result = Result.Success(
                Location(
                    lat = lastLocation.latitude,
                    lon = lastLocation.longitude,
                )
            )
            emit(result)
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    override fun getAddressFromLocation(location: Location): Result<String> {
        return try {
            val addresses = geocoder.getFromLocation(
                location.lat, location.lon, 1
            ) as List<Address>
            val address = addresses[0].getAddressLine(0)
            Result.Success(address)
        } catch (e: Exception) {
            Result.Error(e.message.toString())
        }
    }


}