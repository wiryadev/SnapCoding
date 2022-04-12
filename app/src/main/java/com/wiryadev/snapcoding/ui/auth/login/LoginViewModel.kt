package com.wiryadev.snapcoding.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.response.LoginResult
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val repository: SnapRepository,
) : ViewModel() {

    var animationTriggered = false

    fun login(
        email: String,
        password: String,
    ): LiveData<LoginUiState> {
        return repository.login(email, password).map { result ->
            when (result) {
                is Result.Success -> {
                    LoginUiState(
                        loginResult = result.data,
                        isLoading = false,
                    )
                }
                is Result.Error -> {
                    LoginUiState(
                        errorMessages = result.errorMessage,
                        isLoading = false,
                    )
                }
                is Result.Loading -> {
                    LoginUiState(isLoading = true)
                }
            }
        }.asLiveData()
    }

    fun saveUser(userSession: UserSessionModel) {
        viewModelScope.launch {
            pref.saveUserSession(userSession)
        }
    }

}

data class LoginUiState(
    val loginResult: LoginResult? = null,
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)