package com.android.tiltcamera.camera.presentation

import android.Manifest
import android.graphics.Bitmap
import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tiltcamera.camera.domain.sensor.OrientationSensor
import com.android.tiltcamera.camera.domain.use_case.CameraUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val orientationSensor: OrientationSensor,
    private val cameraUseCases: CameraUseCases,
): ViewModel() {

    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state.asStateFlow()

    private var azimuth by mutableDoubleStateOf(0.0)
    private var pitch by mutableDoubleStateOf(0.0)
    private var roll by mutableDoubleStateOf(0.0)



    init {
        Timber.d("init")


        orientationSensor.startListening()
        orientationSensor.setOnSensorValuesChangedListener { values ->
            azimuth = values[0] * 180 / Math.PI
            pitch = values[1] * 180 / Math.PI
            roll = values[2] * 180 / Math.PI

            _state.update { it.copy(
                azimuth = azimuth,
                pitch = pitch,
                roll = roll
            ) }

            val formattedString = String.format(Locale.getDefault(), "Azimuth=%.2f°, Pitch=%.2f°, Roll=%.2f°", azimuth, pitch, roll)
//            Timber.d(formattedString)

        }

    }


    fun onAction(action: CameraAction) {
        when(action){
            is CameraAction.OnTakePhoto -> onTakePhoto(action.bitmap)

        }
    }


    private fun onTakePhoto(bitmap: Bitmap) {
        // save to storage and to db
        Timber.d("onTakePhoto")
        viewModelScope.launch {
            val result = cameraUseCases.savePhotoUseCase(bitmap)
            Timber.d("result: $result")
        }
    }

}