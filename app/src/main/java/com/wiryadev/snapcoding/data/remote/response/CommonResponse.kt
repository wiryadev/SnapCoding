package com.wiryadev.snapcoding.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wiryadev.snapcoding.model.CommonModel

@JsonClass(generateAdapter = true)
data class CommonResponse(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "message")
    val message: String,
)

fun CommonResponse.asCommonModel() = CommonModel(
    error = error,
    message = message,
)