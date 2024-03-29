package com.wiryadev.snapcoding.ui.auth.register

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.data.repository.story.StoryRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {

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
            authRepository.register(name, email, password).collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = RegisterUiState(
                            isLoading = false,
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = RegisterUiState(
                            errorMessages = result.errorMessage,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }
}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)