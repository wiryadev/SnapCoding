package com.wiryadev.snapcoding

import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.response.*

object DataDummy {

    fun generateDummyUserSession() = UserSessionModel(
        name = "name",
        token = "token",
        isLoggedIn = true
    )

    fun generateSuccessRegisterResponse() = CommonResponse(
        error = false,
        message = "User Created"
    )

    fun generateSuccessLoginResponse() = LoginResponse(
        error = false,
        message = "success",
        loginResult = generateSuccessLoginResult(),
    )

    fun generateSuccessLoginResult() = LoginDto(
        name = "Arif Faizin",
        userId = "user-yj5pc_LARC_AgK61",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
    )

    fun generateSuccessStoriesResponse() = StoriesResponse(
        error = false,
        message = "success",
        listStory = generateListStory()
    )

    fun generateSuccessUploadResponse() = CommonResponse(
        error = false,
        message = "success"
    )

    private fun generateListStory(): List<StoryDto> {
        val stories = mutableListOf<StoryDto>()
        for (i in 1..10) {
            stories.add(
                StoryDto(
                    id = "story-FvU4u0Vp2S3PMsFg",
                    name = "Dimas",
                    description = "Lorem Ipsum",
                    _photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                    createdAt = "2022-01-08T06:34:18.598Z",
                    lat = -10.212,
                    lon = -16.002
                )
            )
        }
        return stories
    }
}