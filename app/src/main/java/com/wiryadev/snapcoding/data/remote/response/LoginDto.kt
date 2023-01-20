package com.wiryadev.snapcoding.data.remote.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.wiryadev.snapcoding.model.User

@JsonClass(generateAdapter = true)
data class LoginDto(
    @Json(name = "userId")
    val userId: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "token")
    val token: String,
)

fun LoginDto.asUser() = User(
    userId = userId,
    name = name,
    token = token,
)