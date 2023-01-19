package com.wiryadev.snapcoding.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _user = MutableLiveData<Result<User>>(null)
    val user: LiveData<Result<User>> get() = _user

    fun getUser() {
        viewModelScope.launch {
            authRepository.getLoggedInUser().collect { result ->
                _user.value = result
            }
        }
    }
}