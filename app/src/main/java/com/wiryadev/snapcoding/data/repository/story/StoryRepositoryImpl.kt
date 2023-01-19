package com.wiryadev.snapcoding.data.repository.story

import android.util.Log
import androidx.paging.*
import com.wiryadev.snapcoding.common.IoDispatcher
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.local.entity.StoryEntity
import com.wiryadev.snapcoding.data.local.entity.asStory
import com.wiryadev.snapcoding.data.local.room.SnapDatabase
import com.wiryadev.snapcoding.data.remote.request.StoryUploadRequest
import com.wiryadev.snapcoding.data.remote.response.StoryDto
import com.wiryadev.snapcoding.data.remote.response.asCommonModel
import com.wiryadev.snapcoding.data.remote.response.asStory
import com.wiryadev.snapcoding.data.remote.retrofit.StoryService
import com.wiryadev.snapcoding.model.CommonModel
import com.wiryadev.snapcoding.model.Story
import com.wiryadev.snapcoding.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StoryRepositoryImpl @Inject constructor(
    private val storyService: StoryService,
    private val snapDatabase: SnapDatabase,
    @IoDispatcher private val dispatcher: CoroutineDispatcher,
) : StoryRepository {

    @ExperimentalPagingApi
    override fun getStories(): Flow<PagingData<Story>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
            ),
            remoteMediator = StoryRemoteMediator(
                apiService = storyService,
                database = snapDatabase,
            ),
            pagingSourceFactory = {
                snapDatabase.snapDao().getStories()
            }
        ).flow.map { pagingData ->
            pagingData.map(StoryEntity::asStory)
        }
    }

    override fun getStoriesWithLocation(): Flow<Result<List<Story>>> = flow {
        wrapEspressoIdlingResource {
            try {
                Log.d("StoryRepository", "getStoriesWithLocation: ")
                val response = storyService.getStoriesForMap()
                Log.d("StoryRepository", "response: $response")
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    val data = responseBody.listStory.map(StoryDto::asStory)
                    emit(Result.Success(data))
                } else {
                    emit(Result.Error(response.message() ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }.flowOn(dispatcher)

    override fun getStoryById(id: String): Flow<Result<Story>> = flow {
        try {
            val response = storyService.getStoryById(id)
            val responseBody = response.body()
            if (response.isSuccessful && responseBody != null) {
                emit(Result.Success(responseBody.asStory()))
            } else {
                emit(Result.Error(response.errorBody().toString()))
            }
        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }.flowOn(dispatcher)

    override fun uploadStory(
        uploadRequest: StoryUploadRequest,
    ): Flow<Result<CommonModel>> = flow {
        wrapEspressoIdlingResource {
            try {
                val response = storyService.uploadImage(
                    file = uploadRequest.photo,
                    description = uploadRequest.description,
                    lat = uploadRequest.lat,
                    lon = uploadRequest.lon,
                )
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    emit(Result.Success(responseBody.asCommonModel()))
                } else {
                    emit(Result.Error(responseBody?.message ?: "Unknown error"))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }
    }.flowOn(dispatcher)

}

private const val PAGE_SIZE = 20