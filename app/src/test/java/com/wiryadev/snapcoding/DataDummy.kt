package com.wiryadev.snapcoding

import com.wiryadev.snapcoding.data.remote.response.LoginResponse
import com.wiryadev.snapcoding.data.remote.response.LoginResult

object DataDummy {
    fun generateSuccessLoginResponse() = LoginResponse(
        error = false,
        message = "success",
        loginResult = generateSuccessLoginResult(),
    )

    fun generateSuccessLoginResult() = LoginResult(
        name = "Arif Faizin",
        userId = "user-yj5pc_LARC_AgK61",
        token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLXlqNXBjX0xBUkNfQWdLNjEiLCJpYXQiOjE2NDE3OTk5NDl9.flEMaQ7zsdYkxuyGbiXjEDXO8kuDTcI__3UjCwt6R_I"
    )
}