package com.wiryadev.snapcoding.utils

import android.app.Application
import android.graphics.Bitmap
import android.graphics.Matrix
import androidx.camera.lifecycle.ProcessCameraProvider
import com.google.common.util.concurrent.ListenableFuture
import com.wiryadev.snapcoding.R
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

private const val FILENAME_FORMAT = "dd-MMM-yyyy"

private val timeStamp: String = SimpleDateFormat(
    FILENAME_FORMAT,
    Locale.US
).format(System.currentTimeMillis())

fun ListenableFuture<ProcessCameraProvider>.addListenerKtx(executor: Executor, listener: Runnable) {
    this.addListener(listener, executor)
}

fun createFile(application: Application): File {
    val mediaDir = application.externalMediaDirs.firstOrNull()?.let {
        File(it, application.resources.getString(R.string.app_name)).apply { mkdirs() }
    }

    val outputDirectory = if (mediaDir?.exists() == true) {
        mediaDir
    } else {
        application.filesDir
    }

    return File(outputDirectory, "$timeStamp.jpg")
}

fun rotateBitmap(bitmap: Bitmap, isBackCamera: Boolean = false): Bitmap {
    val matrix = Matrix().apply {
        if (isBackCamera) {
            postRotate(BACK_CAMERA_ROTATION)
        } else {
            postRotate(FRONT_CAMERA_ROTATION)
            postScale(-1f, 1f, bitmap.width / 2f, bitmap.height / 2f)
        }
    }

    return Bitmap.createBitmap(
        bitmap,
        0,
        0,
        bitmap.width,
        bitmap.height,
        matrix,
        true
    )
}

private const val BACK_CAMERA_ROTATION = 90f
private const val FRONT_CAMERA_ROTATION = -90f