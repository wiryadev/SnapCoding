package com.wiryadev.snapcoding.ui.stories.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.remote.request.StoryUploadRequest
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
import com.wiryadev.snapcoding.model.Location
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: StoryRepository,
) : ViewModel() {

    private val _file: MutableLiveData<File?> = MutableLiveData(null)
    val file: LiveData<File?> get() = _file

    private val _location: MutableLiveData<Location?> = MutableLiveData(null)
    val location: LiveData<Location?> get() = _location

    private val _uiState: MutableLiveData<UploadUiState> = MutableLiveData()
    val uiState: LiveData<UploadUiState> get() = _uiState

    fun assignFile(newFile: File) {
        _file.postValue(newFile)
    }

    fun assignLocation(location: Location?) {
        _location.value = location
    }

    fun upload(
        uploadRequest: StoryUploadRequest,
    ) {
        _uiState.value = UploadUiState.Loading

        viewModelScope.launch {
            repository.uploadStory(uploadRequest).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = UploadUiState.Success
                    }
                    is Result.Error -> {
                        _uiState.value = UploadUiState.Error(result.errorMessage)
                    }
                }
            }
        }
    }

}

sealed interface UploadUiState {
    object Loading : UploadUiState

    class Error(val message: String) : UploadUiState

    object Success : UploadUiState
}