package com.wiryadev.snapcoding.data.local.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wiryadev.snapcoding.data.local.entity.StoryEntity
import com.wiryadev.snapcoding.data.remote.response.StoryDto

@Dao
interface SnapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<StoryEntity>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, StoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}