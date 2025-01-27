package com.android.tiltcamera.camera.domain.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.Sensor.TYPE_ACCELEROMETER
import android.hardware.Sensor.TYPE_MAGNETIC_FIELD
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import timber.log.Timber

class OrientationSensor(
    private val context: Context,
    private val sensorFeature: String,
    sensorType: Int
): MeasurableSensor(sensorType),SensorEventListener {


    private lateinit var sensorManager: SensorManager
    private var accelerometerSensor: Sensor? = null
    private var magneticFieldSensor: Sensor? = null

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    override val doesSensorExist: Boolean
        get() = context.packageManager.hasSystemFeature(sensorFeature)

    override fun startListening() {
        if(!doesSensorExist) {
            Timber.d("$sensorFeature does not exist")
            return
        }
        if(!::sensorManager.isInitialized) {
            sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        }
        if(::sensorManager.isInitialized && accelerometerSensor == null) {
            Timber.d("Initializing accelerometer sensor")
            accelerometerSensor = sensorManager.getDefaultSensor(TYPE_ACCELEROMETER)
        }
        if(::sensorManager.isInitialized && magneticFieldSensor == null) {
            Timber.d("Initializing magneticField sensor")
            magneticFieldSensor = sensorManager.getDefaultSensor(TYPE_MAGNETIC_FIELD)
        }

        accelerometerSensor?.let {
            Timber.d("Registering accelerometerSensor sensor")
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL)
        }
        magneticFieldSensor?.let {
            Timber.d("Registering accelerometerSensor sensor")
            sensorManager.registerListener(
                this,
                it,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_NORMAL)
        }



    }

    override fun stopListening() {
        if(!doesSensorExist || !::sensorManager.isInitialized) {
            Timber.d("Sensor does not exist")
            return
        }
        Timber.d("Unregistering sensor")
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent) {
        if(!doesSensorExist) {
            Timber.d("Sensor does not exist")
            return
        }
        if (event.sensor.type == TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (event.sensor.type == TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()

        onSensorValuesChanged?.invoke(orientationAngles.toList())


    }

    // Compute the three orientation angles based on the most recent readings from
    // the device's accelerometer and magnetometer.
    fun updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(
            rotationMatrix,
            null,
            accelerometerReading,
            magnetometerReading
        )

        // "rotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles)

        // "orientationAngles" now has up-to-date information.
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
        // You must implement this callback in your code.
    }



}