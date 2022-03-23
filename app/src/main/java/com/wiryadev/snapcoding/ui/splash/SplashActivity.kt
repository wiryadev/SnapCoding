package com.wiryadev.snapcoding.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.ui.auth.AuthActivity
import com.wiryadev.snapcoding.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        object : ViewModelProvider.NewInstanceFactory() {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return SplashViewModel(pref = UserPreference.getInstance(applicationContext.dataStore)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        viewModel.getUser().observe(this) { user ->
            startActivity(
                Intent(
                    this,
                    if (user.isLoggedIn) {
                        MainActivity::class.java
                    } else {
                        AuthActivity::class.java
                    }
                )
            )
        }
    }
}