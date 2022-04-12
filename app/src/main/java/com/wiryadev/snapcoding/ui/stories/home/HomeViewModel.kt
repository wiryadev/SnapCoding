package com.wiryadev.snapcoding.ui.stories.home

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.network.SnapCodingApiConfig
import com.wiryadev.snapcoding.data.remote.response.Story
import com.wiryadev.snapcoding.utils.getErrorResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeViewModel(
    private val pref: UserPreference,
    private val repository: SnapRepository
) : ViewModel() {

//    private val _uiState: MutableLiveData<HomeUiState> = MutableLiveData()
//    val uiState: LiveData<HomeUiState> get() = _uiState

    private val authUser = MutableLiveData<UserSessionModel>()

    init {
        getUser()
    }

    //    fun getAllStories(token: String): LiveData<PagingData<Story>> {
//        val authToken = "Bearer $token"
//        return repository.getStories(token = authToken).asLiveData().cachedIn(viewModelScope)
//    }

    @ExperimentalPagingApi
    val stories: LiveData<PagingData<Story>> = authUser.switchMap {
        val authToken = "Bearer ${authUser.value?.token}"
        Log.d("StoryAdapter", "authToken: $authToken")
        repository.getStories(token = authToken).asLiveData().cachedIn(viewModelScope)
    }

    private fun getUser() {
        viewModelScope.launch {
            pref.getUserSession().collectLatest {
                authUser.value = it
            }
        }
    }

}

//data class HomeUiState(
//    val stories: List<Story> = emptyList(),
//    val token: String? = null,
//    val isLoading: Boolean = false,
//    val errorMessages: String? = null,
//)