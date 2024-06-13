package com.seamfix.seamfix.presentation.ui.activities


import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.seamfix.seamfix.R
import com.seamfix.seamfix.data.remote.dto.LocationDTO
import com.seamfix.seamfix.data.remote.dto.SOSRequestDTO
import com.seamfix.seamfix.databinding.ActivityMainBinding
import com.seamfix.seamfix.presentation.viewModel.SeamfixViewModel
import com.seamfix.seamfix.presentation.viewState.Resource
import com.seamfix.seamfix.utils.AppConstants.REQUEST_PERMISSIONS_CODE
import com.seamfix.seamfix.utils.HelperUtil
import com.seamfix.seamfix.utils.HelperUtil.encodeImageToBase64
import com.seamfix.seamfix.utils.HelperUtil.showToast
import dagger.hilt.android.AndroidEntryPoint
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val seamfixViewModel by viewModels<SeamfixViewModel>()
    private lateinit var cameraView: CameraView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var currentLocation: android.location.Location? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cameraView = binding.cameraView

        /**
         * Hosting the camera component in this activity
         * The camera component is bound to the host lifecycle
         * */
        cameraView.setLifecycleOwner(this)

        /**
         * Initializing the location client
         * */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        binding.captureButton.setOnClickListener {
            captureImage()
        }

        /**
         * Request for permissions
         * */
        requestPermissions()
    }

    private fun requestPermissions() {
        val permissions =
            arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION)

        if (!hasPermissions(*permissions)) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSIONS_CODE)
        } else {
            getLocation()
        }
    }

    private fun hasPermissions(vararg permissions: String): Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_CODE) {
            if (hasPermissions(*permissions)) {
                getLocation()
            } else {
                showToast(this, resources.getString(R.string.permissions_denied))
            }
        }
    }

    override fun onStart() {
        super.onStart()
        cameraView.takePicture()
    }

    override fun onResume() {
        super.onResume()
        cameraView.takePicture()
    }

    override fun onStop() {
        super.onStop()
        cameraView.close()
    }

    /**
     * capture image and convert to base64
     * */
    private fun captureImage() {
        cameraView.takePicture()
        cameraView.addCameraListener(object : CameraListener() {
            override fun onPictureTaken(result: PictureResult) {
                result.toBitmap { bitmap ->
                    if (bitmap != null) {
                        val encodedImage = encodeImageToBase64(bitmap)
                        sendSOS(encodedImage)
                    }
                }
            }
        })
    }

    /**
     * Gets user's location after granting permissions
     * */
    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener(this, OnSuccessListener { location ->
            if (location != null) {
                currentLocation = location
            }
        })
    }

    private fun sendSOS(encodedImage: String) {
        if (currentLocation == null) {
            showToast(this, resources.getString(R.string.location_is_not_available))
            return
        }

        val longitude = currentLocation!!.longitude.toString()
        val latitude = currentLocation!!.latitude.toString()

        binding.progressBar.visibility = View.VISIBLE
        binding.captureButton.visibility = View.GONE
        val sosRequest = SOSRequestDTO(
            encodedImage,
            LocationDTO(longitude, latitude),
            listOf("08123456789", "07043255687", "09187655129")
        )


        sendSOSRequest(sosRequest)

    }

    /**
     * makes the sos api call
     * */
    private fun sendSOSRequest(sosRequest: SOSRequestDTO){
        seamfixViewModel.sendSOSRequest(sosRequest)
        seamfixViewModel.sosResponse.observe(this) {
            when (it) {
                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.VISIBLE
                    showToast(this, resources.getString(R.string.success_message))
                }

                is Resource.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.captureButton.visibility = View.VISIBLE
                    showToast(this, resources.getString(R.string.failed_message))
                }
                else -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
    }
}
