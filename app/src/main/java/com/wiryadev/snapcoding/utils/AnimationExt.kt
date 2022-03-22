package com.wiryadev.snapcoding.utils

import android.animation.ObjectAnimator
import android.view.View
import android.widget.ImageView

fun View.animateAlpha(
    animDuration: Long? = null
): ObjectAnimator = ObjectAnimator
    .ofFloat(this, View.ALPHA, 1f)
    .setDuration(animDuration ?: FADE_DURATION)

fun <T : ImageView> T.animateBannerTranslationX(
    animDuration: Long? = null
): ObjectAnimator = ObjectAnimator
    .ofFloat(this, View.TRANSLATION_X, -30f, 30f).apply {
        duration = animDuration ?: BANNER_ANIMATION_DURATION
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
    }


private const val FADE_DURATION = 150L
const val START_DELAY_DURATION = 500L
private const val BANNER_ANIMATION_DURATION = 5000L