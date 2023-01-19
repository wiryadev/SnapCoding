package com.wiryadev.snapcoding.ui.stories.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.load
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.remote.request.StoryUploadRequest
import com.wiryadev.snapcoding.databinding.ActivityUploadBinding
import com.wiryadev.snapcoding.ui.stories.MainActivity
import com.wiryadev.snapcoding.utils.*
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private val viewModel by viewModels<UploadViewModel>()

    private val launcherCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val cameraFile = it.data?.getSerializableExtra(
                CameraActivity.EXTRA_CAMERA
            ) as File

            val isBackCamera = it.data?.getBooleanExtra(
                CameraActivity.EXTRA_CAMERA_POSITION, true
            ) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(cameraFile.path),
                isBackCamera
            )

            val os = BufferedOutputStream(FileOutputStream(cameraFile))
            result.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.close()

            viewModel.assignFile(cameraFile)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val galleryFile = uriToFile(selectedImg, this)
            viewModel.assignFile(galleryFile)
        }
    }

    private val launcherGalleryApi30 = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            val galleryFile = uriToFile(it, this)
            viewModel.assignFile(galleryFile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }

        viewModel.uiState.observe(this) { uiState ->
            with(binding) {
                showLoading(uiState.isLoading)

                uiState.errorMessages?.let { error ->
                    root.showSnackbar(error)
                }

                if (!uiState.isLoading && uiState.errorMessages.isNullOrEmpty()) {
                    root.showSnackbar(getString(R.string.success_create_story))
                    val intent = Intent(this@UploadActivity, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    startActivity(intent)
                    finish()
                }
            }
        }

        viewModel.file.observe(this) { file ->
            with(binding) {
                btnUpload.setOnClickListener {
                    validateUpload(file)
                }
                if (file != null) {
                    val bitmap = BitmapFactory.decodeFile(file.path)
                    ivPicture.load(bitmap)
                }
            }
        }

        with(binding) {
            btnCamera.setOnClickListener {
                val intent = Intent(this@UploadActivity, CameraActivity::class.java)
                launcherCameraX.launch(intent)
            }
            btnGallery.setOnClickListener {
                takePhotoFromGallery()
            }
        }
    }

    private fun takePhotoFromGallery() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            launcherGalleryApi30.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        } else {
            val intent = Intent().apply {
                action = Intent.ACTION_GET_CONTENT
                type = "image/*"
            }
            val chooser =
                Intent.createChooser(intent, getString(R.string.gallery_chooser_title))
            launcherGallery.launch(chooser)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                binding.root.showSnackbar(
                    getString(R.string.permission_not_granted)
                )
                finish()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        with(binding) {
            animateProgressAndButton(
                isLoading = isLoading,
                button = btnUpload,
                progressBar = progressBar,
            )
        }
    }

    private fun validateUpload(file: File?) {
        with(binding) {
            when {
                etDesc.text.toString().isBlank() -> {
                    root.showSnackbar(getString(R.string.error_desc_empty))
                }
                file == null -> {
                    root.showSnackbar(getString(R.string.error_image_empty))
                }
                else -> {
                    val compressedFile = reduceFileImage(file)
                    val requestDescription =
                        etDesc.text.toString().toRequestBody("text/plain".toMediaType())
                    val requestImageFile =
                        compressedFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile
                    )
                    val uploadRequest = StoryUploadRequest(
                        photo = imageMultipart,
                        description = requestDescription,
                        lat = null,
                        lon = null,
                    )
                    viewModel.upload(uploadRequest)
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 100
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10

        private const val DELAY_SUCCESS_INTENT = 1500L
    }
}