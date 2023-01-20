package com.wiryadev.snapcoding.data.remote.retrofit

import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import com.wiryadev.snapcoding.data.remote.response.StoriesResponse
import com.wiryadev.snapcoding.data.remote.response.StoryDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface StoryService {

    @GET("stories")
    suspend fun getAllStories(
        @Query("page") page: Int,
        @Query("size") size: Int,
    ): StoriesResponse

    @GET("stories?location=1")
    suspend fun getStoriesForMap(): Response<StoriesResponse>

    @GET("stories/{id}")
    suspend fun getStoryById(
        @Path("id") id: String,
    ): Response<StoryDto>

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Float?,
        @Part("lon") lon: Float?,
    ): Response<CommonResponse>

}