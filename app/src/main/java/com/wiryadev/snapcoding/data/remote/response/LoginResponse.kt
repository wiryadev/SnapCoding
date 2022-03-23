package com.wiryadev.snapcoding.data.remote.response

data class LoginResponse(
    val error: Boolean,
    val message: String,
    val loginResult: LoginResult,
)