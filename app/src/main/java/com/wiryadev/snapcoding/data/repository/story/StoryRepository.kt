package com.wiryadev.snapcoding.data.repository.story

import androidx.paging.PagingData
import com.wiryadev.snapcoding.data.remote.request.StoryUploadRequest
import com.wiryadev.snapcoding.model.CommonModel
import com.wiryadev.snapcoding.model.Story
import kotlinx.coroutines.flow.Flow
import com.wiryadev.snapcoding.data.Result

interface StoryRepository {

    fun getStories(): Flow<PagingData<Story>>

    fun getStoriesWithLocation(): Flow<Result<List<Story>>>

    fun getStoryById(id: String): Flow<Result<Story>>

    fun uploadStory(storyUploadRequest: StoryUploadRequest): Flow<Result<CommonModel>>

}