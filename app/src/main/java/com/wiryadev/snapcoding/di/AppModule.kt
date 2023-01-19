package com.wiryadev.snapcoding.di

import com.wiryadev.snapcoding.data.repository.auth.AuthRepository
import com.wiryadev.snapcoding.data.repository.auth.AuthRepositoryImpl
import com.wiryadev.snapcoding.data.repository.story.StoryRepository
import com.wiryadev.snapcoding.data.repository.story.StoryRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AppModule {

    @Binds
    @Singleton
    fun bindsAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    fun bindsStoryRepository(storyRepositoryImpl: StoryRepositoryImpl): StoryRepository

}