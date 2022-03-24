package com.wiryadev.snapcoding.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.ui.ViewModelFactory
import com.wiryadev.snapcoding.ui.auth.AuthActivity
import com.wiryadev.snapcoding.ui.main.MainActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore))
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
            finish()
        }
    }
}