package com.wiryadev.snapcoding.data.preference.user

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class UserPreference @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun getUserSession(): Flow<UserSessionModel> = dataStore.data.map { pref ->
        UserSessionModel(
            userId = pref[USER_ID_KEY] ?: "",
            name = pref[NAME_KEY] ?: "",
            token = pref[TOKEN_KEY] ?: "",
        )
    }

    suspend fun saveUserSession(user: UserSessionModel) {
        dataStore.edit { pref ->
            pref[NAME_KEY] = user.name
            pref[TOKEN_KEY] = user.token
        }
    }

    suspend fun deleteUserSession() {
        dataStore.edit { pref ->
            pref.clear()
        }
    }

    companion object {
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")
        private val TOKEN_KEY = stringPreferencesKey("token")
    }
}