package com.wiryadev.snapcoding.ui.splash

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.ui.ViewModelFactory
import com.wiryadev.snapcoding.ui.auth.AuthActivity
import com.wiryadev.snapcoding.ui.stories.StoryActivity

class SplashActivity : AppCompatActivity() {

    private val viewModel by viewModels<SplashViewModel> {
        ViewModelFactory(UserPreference.getInstance(dataStore))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContentView(R.layout.activity_splash)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.getUser().observe(this) { user ->
            startActivity(
                Intent(
                    this,
                    if (user.isLoggedIn) {
                        StoryActivity::class.java
                    } else {
                        AuthActivity::class.java
                    }
                )
            )
            finish()
        }
    }
}