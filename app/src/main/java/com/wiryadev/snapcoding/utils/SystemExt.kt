package com.wiryadev.snapcoding.utils

import android.os.Build
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager

@Suppress("DEPRECATION")
fun Window.hideStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        this.insetsController?.hide(WindowInsets.Type.statusBars())
    } else {
        this.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
    }
}