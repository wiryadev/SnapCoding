package com.wiryadev.snapcoding.ui.stories.upload

import android.util.Log
import androidx.lifecycle.*
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.utils.getErrorResponse
import com.wiryadev.snapcoding.utils.reduceFileImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadViewModel(private val pref: UserPreference) : ViewModel() {

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
        file: File,
        description: String,
    ) {
        _uiState.value = UploadUiState(
            isLoading = true
        )
        viewModelScope.launch(Dispatchers.IO) {
            val authToken = "Bearer $token"

            val compressedFile = reduceFileImage(file)
            val requestDescription = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )

            try {
                val response = SnapCodingApiConfig.getService().uploadImage(
                    token = authToken,
                    file = imageMultipart,
                    description = requestDescription
                )
                if (response.isSuccessful) {
                    _uiState.postValue(
                        UploadUiState(
                            isLoading = false,
                            errorMessages = null
                        )
                    )
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _uiState.postValue(
                        UploadUiState(
                            isLoading = false,
                            errorMessages = getErrorResponse(response.errorBody()!!.string()),
                        )
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, "onFailure: ${e.message}")
                _uiState.postValue(
                    UploadUiState(
                        isLoading = false,
                        errorMessages = e.message,
                    )
                )
            }
        }
    }

    companion object {
        private const val TAG = "UploadViewModel"
    }
}

data class UploadUiState(
    val isLoading: Boolean = false,
    val errorMessages: String? = null,
)