package com.wiryadev.snapcoding.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wiryadev.snapcoding.data.local.entity.StoryEntity
import com.wiryadev.snapcoding.model.Location
import com.wiryadev.snapcoding.model.Story

@JsonClass(generateAdapter = true)
data class StoryDto(
    @Json(name = "id")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "photoUrl")
    val photoUrl: String,
    @Json(name = "createdAt")
    val createdAt: String,
    @Json(name = "lat")
    val lat: Double? = null,
    @Json(name = "lon")
    val lon: Double? = null,
)

fun StoryDto.asStory() = Story(
    id = id,
    name = name,
    description = description,
    photoUrl = photoUrl,
    createdAt = createdAt,
    if (lat != null && lon != null) {
        Location(lat, lon)
    } else null,
)

fun StoryDto.asStoryEntity() = StoryEntity(
    id = id,
    name = name,
    description = description,
    photoUrl = photoUrl,
    createdAt = createdAt,
    lat = lat,
    lon = lon,
)