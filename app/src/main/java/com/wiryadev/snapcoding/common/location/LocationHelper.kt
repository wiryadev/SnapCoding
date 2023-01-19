package com.wiryadev.snapcoding.common.location

import com.wiryadev.snapcoding.model.Location
import com.wiryadev.snapcoding.data.Result
import kotlinx.coroutines.flow.Flow

interface LocationHelper {

    fun getLastLocation(): Flow<Result<Location>>

    fun getAddressFromLocation(location: Location): Result<String>

}