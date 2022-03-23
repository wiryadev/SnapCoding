package com.wiryadev.snapcoding.data.remote.response


import com.google.gson.annotations.SerializedName

data class StoriesResponse(
    @SerializedName("error")
    val error: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("listStory")
    val listStory: List<Story>,
)