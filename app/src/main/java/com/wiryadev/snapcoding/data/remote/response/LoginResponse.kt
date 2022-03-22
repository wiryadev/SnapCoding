package com.wiryadev.snapcoding.data.remote.response

data class LoginResponse(
    val error: Boolean,
    val loginResult: LoginResult,
    val message: String
)