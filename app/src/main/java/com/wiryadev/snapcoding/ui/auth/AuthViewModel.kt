package com.wiryadev.snapcoding.ui.auth

import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    authRepository: AuthRepository
) : ViewModel() {

    val uiState: StateFlow<AuthUiState> = authRepository.getLoggedInUser()
        .map { result ->
            when (result) {
                is Result.Error -> {
                    AuthUiState.Loaded(isLoggedIn = false)
                }
                is Result.Success -> {
                    AuthUiState.Loaded(isLoggedIn = result.data.token.isNotBlank())
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = AuthUiState.Loading,
        )

}

sealed interface AuthUiState {
    object Loading : AuthUiState
    data class Loaded(val isLoggedIn: Boolean) : AuthUiState
}