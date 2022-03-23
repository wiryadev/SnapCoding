package com.wiryadev.snapcoding.data.remote.network

import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResponse
import com.wiryadev.snapcoding.data.remote.response.StoriesResponse
import retrofit2.Response
import retrofit2.http.*

interface SnapCodingService {


    @FormUrlEncoded
    @POST("register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>


    @GET("stories")
    suspend fun getAllStories(
        @Header("Authorization") token: String,
    ): Response<StoriesResponse>

}