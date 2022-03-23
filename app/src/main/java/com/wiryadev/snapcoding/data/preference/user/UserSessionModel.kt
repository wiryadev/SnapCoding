package com.wiryadev.snapcoding.data.preference.user

data class UserSessionModel(
    val name: String,
    val email: String,
    val token: String,
    val isLoggedIn: Boolean,
)
