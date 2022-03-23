package com.wiryadev.snapcoding.ui.auth.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.utils.getErrorResponse
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    private val _uiState: MutableLiveData<RegisterUiState> = MutableLiveData()
    val uiState: LiveData<RegisterUiState> get() = _uiState

    var animationTriggered = false

    fun registerUser(
        name: String,
        email: String,
        password: String,
    ) {
        _uiState.value = RegisterUiState(
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val response = SnapCodingApiConfig.getService().register(
                    name = name,
                    email = email,
                    password = password,
                )
                if (response.isSuccessful) {
                    _uiState.value = RegisterUiState(
                        isLoading = false,
                        errorMessages = null
                    )
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _uiState.value = RegisterUiState(
                        isLoading = false,
                        errorMessages = getErrorResponse(response.errorBody()!!.string()),
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _uiState.value = RegisterUiState(
                    isLoading = false,
                    errorMessages = e.message,
                )
            }
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)