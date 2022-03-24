package com.wiryadev.snapcoding.data.remote.response


import com.google.gson.annotations.SerializedName

data class Story(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("photoUrl")
    private val _photoUrl: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("lat")
    val lat: Double? = null,
    @SerializedName("lon")
    val lon: Double? = null,
) {
    val photoUrl: String
        get() = _photoUrl.replace(" ", "%20").isHttpsUrl()

    private fun String.isHttpsUrl(): String {
        return if (this.contains("https")) {
            this
        } else {
            this.replace("http", "https")
        }
    }
}