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

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreference private constructor(private val dataStore: DataStore<Preferences>){

    fun getUserSession(): Flow<UserSessionModel> = dataStore.data.map { pref ->
        UserSessionModel(
            name =pref[NAME_KEY] ?: "",
            email = pref[EMAIL_KEY] ?: "",
            token = pref[TOKEN_KEY] ?: "",
            isLoggedIn = pref[STATE_KEY] ?: false,
        )
    }

    suspend fun saveUserSession(user: UserSessionModel) {
        dataStore.edit { pref ->
            pref[NAME_KEY] = user.name
            pref[EMAIL_KEY] = user.email
            pref[TOKEN_KEY] = user.token
            pref[STATE_KEY] = true
        }
    }

    suspend fun deleteUserSession() {
        dataStore.edit { pref ->
            pref[NAME_KEY] = ""
            pref[EMAIL_KEY] = ""
            pref[TOKEN_KEY] = ""
            pref[STATE_KEY] = false
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreference? = null

        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val STATE_KEY = booleanPreferencesKey("state")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}