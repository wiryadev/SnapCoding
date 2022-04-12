package com.wiryadev.snapcoding.data

import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.data.remote.network.SnapCodingService
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResponse
import com.wiryadev.snapcoding.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiService : SnapCodingService {

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<CommonResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return Response.success(
            DataDummy.generateSuccessLoginResponse()
        )
    }

    override suspend fun getAllStories(token: String): Response<StoriesResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Response<CommonResponse> {
        TODO("Not yet implemented")
    }
}