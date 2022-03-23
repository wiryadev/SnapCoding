package com.wiryadev.snapcoding.ui.auth.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.data.remote.response.LoginResult
import com.wiryadev.snapcoding.utils.getErrorResponse
import kotlinx.coroutines.launch

class LoginViewModel(private val pref: UserPreference) : ViewModel() {

    private val _uiState: MutableLiveData<LoginUiState> = MutableLiveData()
    val uiState: LiveData<LoginUiState> get() = _uiState

    var animationTriggered = false

    fun login(
        email: String,
        password: String,
    ) {
        _uiState.value = LoginUiState(
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val response = SnapCodingApiConfig.getService().login(
                    email = email,
                    password = password,
                )
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _uiState.value = LoginUiState(
                        loginResult = responseBody.loginResult,
                        isLoading = false,
                        errorMessages = null,
                    )
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _uiState.value = LoginUiState(
                        isLoading = false,
                        errorMessages = getErrorResponse(response.errorBody()!!.string()),
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _uiState.value = LoginUiState(
                    isLoading = false,
                    errorMessages = e.message,
                )
            }
        }
    }

    fun saveUser(userSession: UserSessionModel) {
        viewModelScope.launch {
            pref.saveUserSession(userSession)
        }
    }

    companion object {
        private const val TAG = "RegisterViewModel"
    }
}

data class LoginUiState(
    val loginResult: LoginResult? = null,
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)