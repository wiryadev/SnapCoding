package com.wiryadev.snapcoding.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.di.Injection
import com.wiryadev.snapcoding.ui.auth.login.LoginViewModel
import com.wiryadev.snapcoding.ui.auth.register.RegisterViewModel
import com.wiryadev.snapcoding.ui.settings.SettingsViewModel
import com.wiryadev.snapcoding.ui.stories.home.HomeViewModel
import com.wiryadev.snapcoding.ui.splash.SplashViewModel
import com.wiryadev.snapcoding.ui.stories.upload.UploadViewModel

class ViewModelFactory(
    private val pref: UserPreference,
    private val context: Context,
) : ViewModelProvider.NewInstanceFactory() {

    val repository = Injection.provideRepository(context)

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref, repository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(pref, repository) as T
            }
            modelClass.isAssignableFrom(UploadViewModel::class.java) -> {
                UploadViewModel(pref, repository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}