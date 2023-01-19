package com.wiryadev.snapcoding.data.remote.request

import okhttp3.MultipartBody
import okhttp3.RequestBody

data class StoryUploadRequest(
    val photo: MultipartBody.Part,
    val description: RequestBody,
    val lat: Float?,
    val lon: Float?,
)
