package com.wiryadev.snapcoding.ui.stories.upload

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.ActivityCameraBinding
import com.wiryadev.snapcoding.utils.addListenerKtx
import com.wiryadev.snapcoding.utils.createFile
import com.wiryadev.snapcoding.utils.hideStatusBar
import com.wiryadev.snapcoding.utils.showSnackbar

class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding

    private var imageCapture: ImageCapture? = null
    private var cameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {
            btnCapture.setOnClickListener { takePhoto() }

            btnSwitchCamera.setOnClickListener {
                cameraSelector = if (cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                    CameraSelector.DEFAULT_FRONT_CAMERA
                } else {
                    CameraSelector.DEFAULT_BACK_CAMERA
                }
                startCamera()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        window.hideStatusBar()
        startCamera()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListenerKtx(ContextCompat.getMainExecutor(this)) {
            val cameraProvider = cameraProviderFuture.get()
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.pvViewFinder.surfaceProvider)
                }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (e: Exception) {
                binding.root.showSnackbar(
                    getString(R.string.failed_camera)
                )
            }
        }
    }

    private fun takePhoto() {
        showLoading(true)
        val imageCapture = imageCapture ?: return
        val photoFile = createFile(application)
        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageSavedCallback {
                override fun onError(e: ImageCaptureException) {
                    showLoading(false)
                    binding.root.showSnackbar(
                        getString(R.string.failed_take_photo)
                    )
                }

                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    binding.root.showSnackbar(
                        getString(R.string.success_take_photo)
                    )

                    val intent = Intent()
                    intent.putExtra(EXTRA_CAMERA, photoFile)
                    intent.putExtra(
                        EXTRA_CAMERA_POSITION,
                        cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA
                    )
                    setResult(UploadActivity.CAMERA_X_RESULT, intent)
                    finish()
                }
            }
        )
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.btnCapture.isVisible = !isLoading
    }

    companion object {
        const val EXTRA_CAMERA = "cameraX"
        const val EXTRA_CAMERA_POSITION = "isBackCamera"
    }
}