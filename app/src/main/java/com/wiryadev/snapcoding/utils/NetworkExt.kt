package com.wiryadev.snapcoding.utils

import com.google.gson.Gson
import com.wiryadev.snapcoding.data.remote.response.CommonResponse

fun getErrorResponse(response: String) = Gson().fromJson(response, CommonResponse::class.java).message