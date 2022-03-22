package com.wiryadev.snapcoding.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel : ViewModel() {

    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response: MutableLiveData<CommonResponse> = MutableLiveData()
    val response: LiveData<CommonResponse> = _response

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        _isLoading.value = true
        val client = SnapCodingApiConfig.getService().register(
            name = name,
            email = email,
            password = password,
        )

        client.enqueue(object : Callback<CommonResponse> {
            override fun onResponse(
                call: Call<CommonResponse>,
                response: Response<CommonResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _response.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<CommonResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}