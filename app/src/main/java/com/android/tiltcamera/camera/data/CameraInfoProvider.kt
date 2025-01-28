package com.android.tiltcamera.camera.data

import android.content.Context
import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Size
import androidx.core.content.ContextCompat.getSystemService
import com.android.tiltcamera.core.domain.CameraError
import com.android.tiltcamera.core.domain.Result
import timber.log.Timber
import javax.inject.Inject

class CameraInfoProvider @Inject constructor(private val context: Context) {

    private val cameraManager = getSystemService(context, CameraManager::class.java)!!

    fun getAvailableCameras(): Result<List<CameraInfo>, CameraError> {
        try {
            val cameraInfoList = mutableListOf<CameraInfo>()

            val cameraIdList = cameraManager.cameraIdList // may be empty
            for (cameraId in cameraIdList) {
                val characteristics = cameraManager.getCameraCharacteristics(cameraId)
                val lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING)
                val supportedResolutions = characteristics.get(
                    CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP
                )?.getOutputSizes(ImageFormat.JPEG)?.toList() ?: emptyList()
                cameraInfoList.add(CameraInfo(cameraId, lensFacing!!, supportedResolutions))
            }

            return Result.Success(cameraInfoList)
        } catch (e: CameraAccessException) {
            Timber.e(e)
            return Result.Error(CameraError.CameraAccessError)
        }

    }


    data class CameraInfo(
        val cameraId: String,
        val lensFacing: Int,
        val supportedResolutions: List<Size>
    )
}