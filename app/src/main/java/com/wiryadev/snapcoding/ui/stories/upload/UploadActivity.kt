package com.wiryadev.snapcoding.ui.stories.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.data.preference.user.UserPreference
import com.wiryadev.snapcoding.data.preference.user.dataStore
import com.wiryadev.snapcoding.databinding.ActivityUploadBinding
import com.wiryadev.snapcoding.ui.ViewModelFactory
import com.wiryadev.snapcoding.ui.stories.StoryActivity
import com.wiryadev.snapcoding.utils.animateProgressAndButton
import com.wiryadev.snapcoding.utils.rotateBitmap
import com.wiryadev.snapcoding.utils.showSnackbar
import com.wiryadev.snapcoding.utils.uriToFile
import kotlinx.coroutines.delay
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream


class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private val viewModel by viewModels<UploadViewModel> {
        ViewModelFactory(UserPreference.getInstance(baseContext.dataStore))
    }

    private var file: File? = null
    private var token: String? = null

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

            file = cameraFile

            binding.ivPicture.setImageBitmap(result)
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            val galleryFile = uriToFile(selectedImg, this)
            file = galleryFile
            binding.ivPicture.setImageURI(selectedImg)
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

        viewModel.getUser().observe(this) {
            if (it != null && token.isNullOrEmpty()) {
                token = it.token
            }
        }

        viewModel.uiState.observe(this) { uiState ->
            with(binding) {
                showLoading(uiState.isLoading)

                uiState.errorMessages?.let { error ->
                    root.showSnackbar(error)
                }

                if (!uiState.isLoading && uiState.errorMessages.isNullOrEmpty()) {
                    root.showSnackbar(getString(R.string.success_create_story))
                    lifecycleScope.launchWhenStarted {
                        delay(DELAY_SUCCESS_INTENT)
                        val intent = Intent(this@UploadActivity, StoryActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                }
            }
        }

        with(binding) {
            btnCamera.setOnClickListener {
                val intent = Intent(this@UploadActivity, CameraActivity::class.java)
                launcherCameraX.launch(intent)
            }
            btnGallery.setOnClickListener {
                val intent = Intent().apply {
                    action = Intent.ACTION_GET_CONTENT
                    type = "image/*"
                }
                val chooser =
                    Intent.createChooser(intent, getString(R.string.gallery_chooser_title))
                launcherGallery.launch(chooser)
            }
            btnUpload.setOnClickListener {
                validateUpload()
            }
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

    private fun validateUpload() {
        with(binding) {
            when {
                etDesc.text.toString().isBlank() -> {
                    root.showSnackbar(getString(R.string.error_desc_empty))
                }
                file == null -> {
                    root.showSnackbar(getString(R.string.error_desc_empty))
                }
                else -> {
                    token?.let {
                        viewModel.upload(
                            token = it,
                            file = file as File,
                            description = etDesc.text.toString(),
                        )
                    }
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