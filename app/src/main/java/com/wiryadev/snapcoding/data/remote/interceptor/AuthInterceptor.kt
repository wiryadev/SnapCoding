package com.wiryadev.snapcoding.data.remote.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class AuthInterceptor : Interceptor {

    private var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val requestBuilder: Request.Builder = request.newBuilder()

        val needCredential = request.header(NO_AUTH_HEADER_KEY) == null
        if (needCredential) {
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            } ?: run {
                Log.d("AuthInterceptor", "Token is NULL")
            }
        }

        return chain.proceed(requestBuilder.build())
    }

    fun setToken(newToken: String) {
        if (token == null && newToken.isNotEmpty()) {
            token = newToken
        }
    }

    fun deleteToken() {
        token = null
    }

    companion object {
        const val NO_AUTH_HEADER_KEY = "No-Authentication"
    }
}