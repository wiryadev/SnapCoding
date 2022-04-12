package com.wiryadev.snapcoding.ui.stories.upload

import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.utils.reduceFileImage
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadViewModel(
    private val pref: UserPreference,
    private val repository: SnapRepository,
) : ViewModel() {

    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUserSession().asLiveData()
    }

    private val _file: MutableLiveData<File?> = MutableLiveData(null)
    val file: LiveData<File?> get() = _file

    private val _uiState: MutableLiveData<UploadUiState> = MutableLiveData()
    val uiState: LiveData<UploadUiState> get() = _uiState

    fun assignFile(newFile: File) {
        _file.value = newFile
    }

    fun upload(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
    ) {
        _uiState.value = UploadUiState(
            isLoading = true
        )
        viewModelScope.launch {
            val authToken = "Bearer $token"

            repository.upload(
                token = authToken,
                file = file,
                description = description,
            ).collectLatest { result ->
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