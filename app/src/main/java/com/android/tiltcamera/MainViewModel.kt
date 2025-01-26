package com.android.tiltcamera

import android.hardware.SensorManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.android.tiltcamera.sensor.OrientationSensor
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val orientationSensor: OrientationSensor
): ViewModel() {

    var azimuth by mutableDoubleStateOf(0.0)
    var pitch by mutableDoubleStateOf(0.0)
    var roll by mutableDoubleStateOf(0.0)

    init {
        Timber.d("init")
        orientationSensor.startListening()
        orientationSensor.setOnSensorValuesChangedListener { values ->
            azimuth = values[0] * 180 / Math.PI
            pitch = values[1] * 180 / Math.PI
            roll = values[2] * 180 / Math.PI
            val formattedString = String.format(Locale.getDefault(), "Azimuth=%.2f°, Pitch=%.2f°, Roll=%.2f°", azimuth, pitch, roll)
            Timber.d(formattedString)

//            Timber.d("Azimuth=${(values[0] * 180 / Math.PI)}, Pitch=${values[1] * 180 / Math.PI}, Roll=${values[2] * 180 / Math.PI}")
        }

    }

    private lateinit var sensorManager: SensorManager

    // Rotation matrix based on current readings from accelerometer and magnetometer.

}