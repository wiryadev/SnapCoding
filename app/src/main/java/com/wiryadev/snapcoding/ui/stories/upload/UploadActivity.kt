package com.wiryadev.snapcoding.ui.stories.upload

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wiryadev.snapcoding.R
import com.wiryadev.snapcoding.databinding.ActivityUploadBinding
import com.wiryadev.snapcoding.utils.rotateBitmap
import com.wiryadev.snapcoding.utils.showSnackbar
import com.wiryadev.snapcoding.utils.uriToFile
import java.io.File

class UploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUploadBinding

    private var file: File? = null

    private val launcherCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val cameraFile = it.data?.getSerializableExtra(
                CameraActivity.EXTRA_CAMERA
            ) as File
            file = cameraFile

            val isBackCamera = it.data?.getBooleanExtra(
                CameraActivity.EXTRA_CAMERA_POSITION, true
            ) as Boolean

            val result = rotateBitmap(
                BitmapFactory.decodeFile(file?.path),
                isBackCamera
            )

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

        with(binding) {
            btnCamera.setOnClickListener {
                val intent = Intent(this@UploadActivity, CameraActivity::class.java)
                launcherCameraX.launch(intent)
            }
            btnGallery.setOnClickListener {
                val intent = Intent()
                intent.action = Intent.ACTION_GET_CONTENT
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, getString(R.string.gallery_chooser_title))
                launcherGallery.launch(chooser)
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

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    companion object {
        const val CAMERA_X_RESULT = 100
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}