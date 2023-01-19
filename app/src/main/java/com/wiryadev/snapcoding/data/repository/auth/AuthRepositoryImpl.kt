package com.wiryadev.snapcoding.data.repository.auth

import com.wiryadev.snapcoding.common.IoDispatcher
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.asResultFlow
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.asUser
import com.wiryadev.snapcoding.data.preference.user.asUserSessionModel
import com.wiryadev.snapcoding.data.remote.interceptor.AuthInterceptor
import com.wiryadev.snapcoding.data.remote.response.asCommonModel
import com.wiryadev.snapcoding.data.remote.response.asUser
import com.wiryadev.snapcoding.data.remote.retrofit.AuthService
import com.wiryadev.snapcoding.model.CommonModel
import com.wiryadev.snapcoding.model.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val userPreference: UserPreference,
    private val authInterceptor: AuthInterceptor,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : AuthRepository {

    override fun login(
        email: String,
        password: String,
    ): Flow<Result<User>> = flow {
        try {
            val response = authService.login(
                email = email,
                password = password,
            )
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                val user = responseBody.loginResult.asUser()
                authInterceptor.setToken(user.token)
                emit(Result.Success(user))
            } else {
                emit(Result.Error(response.message() ?: "Unknown Error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    override fun register(
        name: String,
        email: String,
        password: String
    ): Flow<Result<CommonModel>> = flow {
        try {
            val response = authService.register(
                name = name,
                email = email,
                password = password,
            )
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                emit(Result.Success(responseBody.asCommonModel()))
            } else {
                emit(Result.Error(responseBody?.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    override fun getLoggedInUser(): Flow<Result<User>> = userPreference.getUserSession()
        .map {
            if (it.token.isNotEmpty()) {
                authInterceptor.setToken(it.token)
            }
            return@map it.asUser()
        }
        .asResultFlow()

    override fun saveUserSession(user: User): Flow<Result<Unit>> = flow {
        try {
            val block = userPreference.saveUserSession(user.asUserSessionModel())
            emit(Result.Success(block))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    override fun removeUserSession(): Flow<Result<Unit>> = flow {
        try {
            val block = userPreference.deleteUserSession()
            authInterceptor.deleteToken()
            emit(Result.Success(block))
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

}