package com.wiryadev.snapcoding.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.data.remote.response.CommonResponse
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            try {
                val result = SnapCodingApiConfig.getService().register(
                    name = name,
                    email = email,
                    password = password,
                )
                if (result.error) {
                    Log.e(TAG, "onFailure: ${result.message}")
                } else {
                    _response.value = result
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${e.message}")
            }
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}