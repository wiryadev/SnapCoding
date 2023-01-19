package com.wiryadev.snapcoding.ui.stories.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.wiryadev.snapcoding.data.repository.story.StoryRepositoryImpl
import com.wiryadev.snapcoding.model.Story
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: StoryRepositoryImpl
) : ViewModel() {

    @ExperimentalPagingApi
    val stories: LiveData<PagingData<Story>> = repository.getStories()
        .asLiveData()
        .cachedIn(viewModelScope)


}