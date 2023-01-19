package com.wiryadev.snapcoding.data.remote.response

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginDto,
)