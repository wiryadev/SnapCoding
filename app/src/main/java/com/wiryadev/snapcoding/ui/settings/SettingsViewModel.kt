package com.wiryadev.snapcoding.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel
import kotlinx.coroutines.launch

class SettingsViewModel(private val pref: UserPreference) : ViewModel() {

    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUserSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            pref.deleteUserSession()
        }
    }

}