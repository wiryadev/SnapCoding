package com.wiryadev.snapcoding.ui.stories.home

import android.util.Log
import androidx.lifecycle.*
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wiryadev.snapcoding.data.SnapRepository
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import com.wiryadev.snapcoding.data.remote.response.Story

class HomeViewModel(
    private val pref: UserPreference,
    private val repository: SnapRepository
) : ViewModel() {

    private val _token = MutableLiveData<String>()
    var mutableToken = ""

    @ExperimentalPagingApi
    val stories: LiveData<PagingData<Story>> = _token.switchMap {
        val authToken = "Bearer ${_token.value}"
        Log.d("StoryAdapter", "authToken: $authToken")
        repository.getStories(token = authToken).asLiveData().cachedIn(viewModelScope)
    }

    fun setToken(newToken: String) {
        _token.value = newToken
    }

    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUserSession().asLiveData()
    }

}