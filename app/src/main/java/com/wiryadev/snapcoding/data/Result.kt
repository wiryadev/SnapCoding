package com.wiryadev.snapcoding.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

sealed interface Result<out R> {
    data class Success<out T>(val data: T) : Result<T>
    data class Error(val errorMessage: String) : Result<Nothing>
}

fun <T> Flow<T>.asResultFlow(): Flow<Result<T>> = this
    .map<T, Result<T>> {
        Result.Success(it)
    }
    .catch {
        emit(Result.Error(it.message ?: "Unknown Error"))
    }