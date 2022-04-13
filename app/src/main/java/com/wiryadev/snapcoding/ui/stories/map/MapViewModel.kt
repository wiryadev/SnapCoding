package com.wiryadev.snapcoding.ui.stories.map

import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.response.Story
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MapViewModel(
    private val pref: UserPreference,
    private val repository: SnapRepository
) : ViewModel() {

    private val _uiState: MutableLiveData<MapUiState> = MutableLiveData()
    val uiState: LiveData<MapUiState> get() = _uiState

    val user: LiveData<UserSessionModel> get() = pref.getUserSession().asLiveData()

    fun getStoriesForMap(token: String) {
        _uiState.value = MapUiState(
            isLoading = true
        )
        viewModelScope.launch {
            repository.getStoriesForMap(token = "Bearer $token").collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = MapUiState(
                            stories = result.data.listStory,
                            isLoading = false,
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = MapUiState(
                            errorMessages = result.errorMessage,
                            isLoading = false,
                        )
                    }
                }
            }
        }
    }

}

data class MapUiState(
    val stories: List<Story> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)