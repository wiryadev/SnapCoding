package com.wiryadev.snapcoding.data.remote.retrofit

import com.wiryadev.snapcoding.data.remote.interceptor.AuthInterceptor.Companion.NO_AUTH_HEADER_KEY
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface AuthService {

    @FormUrlEncoded
    @POST("register")
    @Headers("$NO_AUTH_HEADER_KEY: true")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<CommonResponse>

    @FormUrlEncoded
    @POST("login")
    @Headers("$NO_AUTH_HEADER_KEY: true")
    suspend fun login(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Response<LoginResponse>

}