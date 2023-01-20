package com.wiryadev.snapcoding.ui.stories.map

import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
import com.wiryadev.snapcoding.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val storyRepository: StoryRepository
) : ViewModel() {

    private val _uiState: MutableLiveData<MapUiState> = MutableLiveData()
    val uiState: LiveData<MapUiState> get() = _uiState

    fun getStoriesWithLocation() {
        _uiState.value = MapUiState(
            isLoading = true
        )
        viewModelScope.launch {
            storyRepository.getStoriesWithLocation().collectLatest { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = MapUiState(
                            stories = result.data,
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