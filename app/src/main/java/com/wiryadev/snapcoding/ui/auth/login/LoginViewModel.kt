package com.wiryadev.snapcoding.ui.auth.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.response.LoginDto
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
import com.wiryadev.snapcoding.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
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
            authRepository.login(email, password).collect { result ->
                when (result) {
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


    fun saveUser(user: User) {
        viewModelScope.launch {
            authRepository.saveUserSession(user).collect { result ->
                if (result is Result.Error) {
                    _uiState.value = LoginUiState(
                        errorMessages = result.errorMessage
                    )
                }
            }
        }
    }

}

data class LoginUiState(
    val loginResult: User? = null,
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)