package com.wiryadev.snapcoding.utils

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar

fun View.animateAlphaToVisible(
    animDuration: Long? = null
): ObjectAnimator = ObjectAnimator
    .ofFloat(this, View.ALPHA, 1f)
    .setDuration(animDuration ?: DEFAULT_FADE_DURATION)

fun View.animateAlphaToInvisible(
    animDuration: Long? = null
): ObjectAnimator = ObjectAnimator
    .ofFloat(this, View.ALPHA, 0f)
    .setDuration(animDuration ?: DEFAULT_FADE_DURATION)

private const val DEFAULT_FADE_DURATION = 150L


fun <T : ImageView> T.animateBannerTranslationX(
    animDuration: Long? = null,
    startPosition: Float? = null,
    endPosition: Float? = null,
): ObjectAnimator = ObjectAnimator
    .ofFloat(
        this,
        View.TRANSLATION_X,
        startPosition ?: DEFAULT_START_TRANSLATION_VALUE,
        endPosition ?: DEFAULT_END_TRANSLATION_VALUE,
    ).apply {
        duration = animDuration ?: DEFAULT_BANNER_TRANSLATION_DURATION
        repeatCount = ObjectAnimator.INFINITE
        repeatMode = ObjectAnimator.REVERSE
    }

private const val DEFAULT_BANNER_TRANSLATION_DURATION = 5000L
private const val DEFAULT_START_TRANSLATION_VALUE = -30F
private const val DEFAULT_END_TRANSLATION_VALUE = 30F

fun animateProgressAndButton(
    isLoading: Boolean,
    button: Button,
    progressBar: ProgressBar
) {
    if (isLoading) {
        AnimatorSet().apply {
            playSequentially(
                button.animateAlphaToInvisible(),
                progressBar.animateAlphaToVisible(),
            )
            startDelay = 200L
        }.start()
    } else {
        AnimatorSet().apply {
            playSequentially(
                progressBar.animateAlphaToInvisible(),
                button.animateAlphaToVisible(),
            )
            startDelay = 200L
        }.start()
    }
}

const val DEFAULT_START_DELAY_DURATION = 500L