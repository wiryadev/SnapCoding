package com.wiryadev.snapcoding.ui.stories.home

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

    private val token = MutableLiveData<String>()

    val user: LiveData<UserSessionModel> = pref.getUserSession().asLiveData()

    @ExperimentalPagingApi
    val stories: LiveData<PagingData<Story>> = token.switchMap {
        val authToken = "Bearer $it"
        repository.getStories(token = authToken).asLiveData().cachedIn(viewModelScope)
    }

    fun setToken(newToken: String) {
        token.value = newToken
    }


}