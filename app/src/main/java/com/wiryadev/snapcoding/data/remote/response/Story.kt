package com.wiryadev.snapcoding.data.remote.response


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "story")
@Parcelize
data class Story(
    @PrimaryKey
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
) : Parcelable {
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