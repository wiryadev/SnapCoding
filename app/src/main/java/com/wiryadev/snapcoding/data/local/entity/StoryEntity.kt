package com.wiryadev.snapcoding.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.wiryadev.snapcoding.model.Story

@Entity(tableName = "story")
data class StoryEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String,
    @ColumnInfo(name = "createdAt")
    val createdAt: String,
    @ColumnInfo(name = "lat")
    val lat: Double? = null,
    @ColumnInfo(name = "lon")
    val lon: Double? = null,
)

fun StoryEntity.asStory() = Story(
    id = id,
    name = name,
    description = description,
    photoUrl = photoUrl,
    createdAt = createdAt,
    lat = lat,
    lon = lon,
)