package com.wiryadev.snapcoding.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.utils.hideStatusBar
import dagger.hilt.android.AndroidEntryPoint
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.ui.stories.MainActivity

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    private val viewModel by viewModels<AuthViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
            .setKeepOnScreenCondition {
                viewModel.user.value == null
            }

        viewModel.getUser()

        viewModel.user.observe(this) {result ->
            if (result is Result.Success) {
                if (result.data.token.isNotBlank()) {
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                }
            }
        }

        setContentView(R.layout.activity_auth)

    }
}