package com.wiryadev.snapcoding.data.preference.user

import com.wiryadev.snapcoding.model.User

data class UserSessionModel(
    val userId: String,
    val name: String,
    val token: String,
)

fun UserSessionModel.asUser() = User(
    userId = userId,
    name = name,
    token = token,
)

fun User.asUserSessionModel() = UserSessionModel(
    userId = userId,
    name = name,
    token = token,
)