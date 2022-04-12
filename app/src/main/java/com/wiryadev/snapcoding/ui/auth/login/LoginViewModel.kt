package com.wiryadev.snapcoding.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.response.LoginResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginViewModel(
    private val pref: UserPreference,
    private val repository: SnapRepository,
) : ViewModel() {

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
            repository.login(email, password).collectLatest { result ->
                when(result) {
                    is Result.Success -> {
                        _uiState.value = LoginUiState(
                            loginResult = result.data,
                            isLoading = false,
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = LoginUiState(
                            errorMessages = result.errorMessage,
                            isLoading = false,
                        )
                    }
                }
            }
        }
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