package com.wiryadev.snapcoding.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wiryadev.snapcoding.data.remote.response.Story

@Dao
interface SnapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(stories: List<Story>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, Story>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}