package com.wiryadev.snapcoding.ui.stories.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import coil.load
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.common.location.LocationHelper
import com.wiryadev.snapcoding.data.Result
import com.wiryadev.snapcoding.data.remote.request.StoryUploadRequest
import com.wiryadev.snapcoding.databinding.ActivityUploadBinding
import com.wiryadev.snapcoding.ui.stories.MainActivity
import com.wiryadev.snapcoding.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@AndroidEntryPoint
class UploadActivity : AppCompatActivity() {

    @Inject
    lateinit var locationHelper: LocationHelper

    private lateinit var binding: ActivityUploadBinding

    private val viewModel by viewModels<UploadViewModel>()

    private val requestLocationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    // Precise location access granted.
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    // Only approximate location access granted.
                    getMyLastLocation()
                }
                else -> {
                    binding.root.showSnackbar(getString(R.string.error_location_not_granted))
                }
            }
        }

    private val launcherCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val cameraFile = it.data?.getSerializableExtra(
                CameraActivity.EXTRA_CAMERA
            ) as File

            val exifInterface = ExifInterface(cameraFile.path)
            val orientation = exifInterface.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED
            )

            val result = rotateBitmap(
                BitmapFactory.decodeFile(cameraFile.path),
                orientation,
            )

            lifecycleScope.launch(Dispatchers.IO) {
                val os = BufferedOutputStream(FileOutputStream(cameraFile))
                result.compress(Bitmap.CompressFormat.PNG, 100, os)
                os.close()

                viewModel.assignFile(cameraFile)
            }
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
            when (uiState) {
                is UploadUiState.Error -> {
                    binding.root.showSnackbar(uiState.message)
                }
                UploadUiState.Loading -> {
                    showLoading()
                }
                UploadUiState.Success -> {
                    binding.root.showSnackbar(getString(R.string.success_create_story))
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

        viewModel.location.observe(this) { location ->
            binding.swLocation.isChecked = location != null
            if (location != null) {
                when (val result = locationHelper.getAddressFromLocation(location)) {
                    is Result.Error -> {
                        binding.root.showSnackbar(result.errorMessage)
                    }
                    is Result.Success -> {
                        binding.tvAddress.text = result.data
                    }
                }
            } else {
                binding.tvAddress.text = ""
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
            swLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    getMyLastLocation()
                } else {
                    viewModel.assignLocation(null)
                }
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

    private fun showLoading() {
        with(binding) {
            animateProgressAndButton(
                isLoading = true,
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
                    val location = viewModel.location.value
                    val uploadRequest = StoryUploadRequest(
                        photo = imageMultipart,
                        description = requestDescription,
                        lat = location?.lat?.toFloat(),
                        lon = location?.lon?.toFloat(),
                    )
                    viewModel.upload(uploadRequest)
                }
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun checkOptionalPermissions() = OPTIONAL_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if (checkOptionalPermissions()) {
            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    locationHelper.getLastLocation().collect { result ->
                        when (result) {
                            is Result.Error -> {
                                binding.root.showSnackbar(result.errorMessage)
                                viewModel.assignLocation(null)
                            }
                            is Result.Success -> {
                                viewModel.assignLocation(result.data)
                            }
                        }
                    }
                }
            }
        } else {
            requestLocationPermissionLauncher.launch(OPTIONAL_PERMISSIONS)
        }
    }

    companion object {
        const val CAMERA_X_RESULT = 100
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private val OPTIONAL_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        )
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}