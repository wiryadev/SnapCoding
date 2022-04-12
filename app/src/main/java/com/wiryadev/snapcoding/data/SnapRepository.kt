package com.wiryadev.snapcoding.data

import android.util.Log
import com.wiryadev.snapcoding.data.remote.network.SnapCodingService
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResult
import com.wiryadev.snapcoding.utils.getErrorResponse
import com.wiryadev.snapcoding.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SnapRepository(
    private val snapCodingService: SnapCodingService,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun login(
        email: String,
        password: String,
    ): Flow<Result<LoginResult>> {
        return flow {
            emit(Result.Loading)
            wrapEspressoIdlingResource {
                try {
                    val response = snapCodingService.login(
                        email = email,
                        password = password,
                    )
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        emit(Result.Success(responseBody.loginResult))
                    } else {
                        emit(Result.Error(getErrorResponse(response.errorBody()!!.string())))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onFailure: ${e.message}")
                    emit(Result.Error(e.message.toString()))
                }
            }
        }.flowOn(dispatcher)
    }

    fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<Result<CommonResponse>> {
        return flow {
            wrapEspressoIdlingResource {
                try {
                    val response = snapCodingService.register(
                        name = name,
                        email = email,
                        password = password,
                    )
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        emit(Result.Success(responseBody))
                    } else {
                        emit(Result.Error(getErrorResponse(response.errorBody()!!.string())))
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "onFailure: ${e.message}")
                    emit(Result.Error(e.message.toString()))
                }
            }
        }.flowOn(dispatcher)
    }

}

private const val TAG = "SnapRepository"