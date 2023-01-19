package com.wiryadev.snapcoding.ui.stories.upload

import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.repository.story.StoryRepositoryImpl
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.request.StoryUploadRequest
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UploadViewModel @Inject constructor(
    private val repository: StoryRepository,
) : ViewModel() {

    private val _file: MutableLiveData<File?> = MutableLiveData(null)
    val file: LiveData<File?> get() = _file

    private val _uiState: MutableLiveData<UploadUiState> = MutableLiveData()
    val uiState: LiveData<UploadUiState> get() = _uiState

    fun assignFile(newFile: File) {
        _file.value = newFile
    }

    fun upload(
        uploadRequest: StoryUploadRequest,
    ) {
        _uiState.value = UploadUiState(
            isLoading = true
        )

        viewModelScope.launch {
            repository.uploadStory(uploadRequest).collect { result ->
                when (result) {
                    is Result.Success -> {
                        _uiState.value = UploadUiState(
                            isLoading = false,
                            errorMessages = null
                        )
                    }
                    is Result.Error -> {
                        _uiState.value = UploadUiState(
                            isLoading = false,
                            errorMessages = result.errorMessage,
                        )
                    }
                }
            }
        }
    }

}

data class UploadUiState(
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)