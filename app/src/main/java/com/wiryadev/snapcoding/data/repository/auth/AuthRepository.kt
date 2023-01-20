package com.wiryadev.snapcoding.data.repository.auth

import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.model.CommonModel
import com.wiryadev.snapcoding.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    fun login(
        email: String,
        password: String,
    ): Flow<Result<User>>

    fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<Result<CommonModel>>

    fun getLoggedInUser(): Flow<Result<User>>

    fun saveUserSession(
        user: User
    ): Flow<Result<Unit>>

    fun removeUserSession(): Flow<Result<Unit>>

}