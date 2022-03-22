package com.wiryadev.snapcoding.ui.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.utils.hideStatusBar

class AuthActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        window?.hideStatusBar()
    }
}