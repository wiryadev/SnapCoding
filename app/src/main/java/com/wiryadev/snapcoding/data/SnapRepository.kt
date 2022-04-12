package com.wiryadev.snapcoding.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.wiryadev.snapcoding.data.local.SnapDatabase
import com.wiryadev.snapcoding.data.remote.network.SnapCodingService
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResult
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.utils.getErrorResponse
import com.wiryadev.snapcoding.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody

class SnapRepository(
    private val snapCodingService: SnapCodingService,
    private val snapDatabase: SnapDatabase,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) {

    fun login(
        email: String,
        password: String,
    ): Flow<Result<LoginResult>> {
        return flow {
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
                    emit(Result.Error(e.message.toString()))
                }
            }
        }.flowOn(dispatcher)
    }

    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
    ): Flow<Result<CommonResponse>> {
        return flow {
            wrapEspressoIdlingResource {
                try {
                    val response = snapCodingService.uploadImage(
                        token = token,
                        file = file,
                        description = description,
                    )
                    val responseBody = response.body()
                    if (response.isSuccessful && responseBody != null) {
                        emit(Result.Success(responseBody))
                    } else {
                        emit(Result.Error(getErrorResponse(response.errorBody()!!.string())))
                    }
                } catch (e: Exception) {
                    emit(Result.Error(e.message.toString()))
                }
            }
        }.flowOn(dispatcher)
    }

    @ExperimentalPagingApi
    fun getStories(token: String): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
            ),
            remoteMediator = SnapRemoteMediator(
                token = token,
                apiService = snapCodingService,
                database = snapDatabase,
            ),
            pagingSourceFactory = {
                snapDatabase.snapDao().getStories()
            }
        ).flow
    }

}

private const val TAG = "SnapRepository"