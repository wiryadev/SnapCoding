package com.wiryadev.snapcoding.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wiryadev.snapcoding.data.remote.response.Story

@Database(
    entities = [
        Story::class,
        RemoteKeys::class,
    ],
    version = 1,
)
abstract class SnapDatabase : RoomDatabase() {

    abstract fun snapDao(): SnapDao

    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: SnapDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): SnapDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room
                    .databaseBuilder(
                        context.applicationContext,
                        SnapDatabase::class.java,
                        "snapcoding_db"
                    )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}