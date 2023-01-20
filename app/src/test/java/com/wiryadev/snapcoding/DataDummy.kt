package com.wiryadev.snapcoding

import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.model.CommonModel
import com.wiryadev.snapcoding.model.Location
import com.wiryadev.snapcoding.model.Story
import com.wiryadev.snapcoding.model.User

object DataDummy {

    fun generateDummyUserSession() = UserSessionModel(
        userId = "userId",
        name = "name",
        token = "token",
    )

    fun generateSuccessCommonModel() = CommonModel(
        error = false,
        message = "User Created"
    )

    fun generateDummyUser() = User(
        name = "Arif Faizin",
        userId = "user-yj5pc_LARC_AgK61",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
    )

    fun generateListStory(): List<Story> {
        val stories = mutableListOf<Story>()
        for (i in 1..10) {
            stories.add(
                Story(
                    id = "story-FvU4u0Vp2S3PMsFg",
                    name = "Dimas",
                    description = "Lorem Ipsum",
                    photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                    createdAt = "2022-01-08T06:34:18.598Z",
                    Location(
                        lat = -10.212,
                        lon = -16.002,
                    )
                )
            )
        }
        return stories
    }
}