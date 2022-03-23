package com.wiryadev.snapcoding.ui.main.home

import android.util.Log
import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.utils.getErrorResponse
import kotlinx.coroutines.launch

class HomeViewModel(private val pref: UserPreference) : ViewModel() {

    private val _uiState: MutableLiveData<HomeUiState> = MutableLiveData()
    val uiState: LiveData<HomeUiState> get() = _uiState

    fun getAllStories(token: String) {
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

    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUserSession().asLiveData()
    }

    companion object {
        private const val TAG = "HomeViewModel"
    }
}

data class HomeUiState(
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)