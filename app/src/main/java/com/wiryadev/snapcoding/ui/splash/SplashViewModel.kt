package com.wiryadev.snapcoding.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.UserSessionModel

class SplashViewModel(private val pref: UserPreference) : ViewModel() {
    fun getUser(): LiveData<UserSessionModel> {
        return pref.getUserSession().asLiveData()
    }
}