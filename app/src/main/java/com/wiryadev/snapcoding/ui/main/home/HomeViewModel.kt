package com.wiryadev.snapcoding.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.utils.getErrorResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(private val pref: UserPreference) : ViewModel() {

    private val _uiState: MutableLiveData<HomeUiState> = MutableLiveData()
    val uiState: LiveData<HomeUiState> get() = _uiState

    init {
        getUser()
    }

    fun getAllStories(token: String) {
        Log.d(TAG, "getAllStories: executed")
        _uiState.value = HomeUiState(
            isLoading = true
        )
        viewModelScope.launch {
            try {
                val response = SnapCodingApiConfig.getService().getAllStories(
                    token = "Bearer $token",
                )
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _uiState.value = HomeUiState(
                        stories = responseBody.listStory,
                        isLoading = false,
                        errorMessages = null,
                    )
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _uiState.value = HomeUiState(
                        isLoading = false,
                        errorMessages = getErrorResponse(response.errorBody()!!.string()),
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _uiState.value = HomeUiState(
                    isLoading = false,
                    errorMessages = e.message,
                )
            }
        }
    }

    private fun getUser() {
        viewModelScope.launch {
            pref.getUserSession().collectLatest {
               _uiState.value = HomeUiState(
                   token = it.token
               )
            }
        }
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}

data class HomeUiState(
    val stories: List<Story> = emptyList(),
    val token: String? = null,
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)