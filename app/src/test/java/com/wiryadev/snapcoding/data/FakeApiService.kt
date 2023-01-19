package com.wiryadev.snapcoding.data

import com.wiryadev.snapcoding.DataDummy
import com.wiryadev.snapcoding.data.remote.retrofit.StoryService
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResponse
import com.wiryadev.snapcoding.data.remote.response.StoriesResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class FakeApiService : StoryService {

    override suspend fun register(
        name: String,
        email: String,
        password: String
    ): Response<CommonResponse> {
        return Response.success(
            DataDummy.generateSuccessRegisterResponse()
        )
    }

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return Response.success(
            DataDummy.generateSuccessLoginResponse()
        )
    }

    override suspend fun getAllStories(
        token: String,
        page: Int,
        size: Int,
    ): StoriesResponse {
        return DataDummy.generateSuccessStoriesResponse()
    }

    override suspend fun getStoriesForMap(token: String, size: Int): Response<StoriesResponse> {
        return Response.success(
            DataDummy.generateSuccessStoriesResponse()
        )
    }

    override suspend fun uploadImage(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody
    ): Response<CommonResponse> {
        return Response.success(
            DataDummy.generateSuccessUploadResponse()
        )
    }
}