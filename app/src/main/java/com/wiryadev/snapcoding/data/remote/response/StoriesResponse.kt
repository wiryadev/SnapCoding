package com.wiryadev.snapcoding.data.remote.response


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class StoriesResponse(
    @Json(name = "error")
    val error: Boolean,
    @Json(name = "message")
    val message: String,
    @Json(name = "listStory")
    val listStory: List<StoryDto>,
)