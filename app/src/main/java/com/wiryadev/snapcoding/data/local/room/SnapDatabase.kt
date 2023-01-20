package com.wiryadev.snapcoding.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.wiryadev.snapcoding.data.local.entity.RemoteKeys
import com.wiryadev.snapcoding.data.local.entity.StoryEntity

@Database(
    entities = [
        StoryEntity::class,
        RemoteKeys::class,
    ],
    version = 1,
)
abstract class SnapDatabase : RoomDatabase() {
    abstract fun snapDao(): StoryDao
    abstract fun remoteKeysDao(): RemoteKeysDao

}