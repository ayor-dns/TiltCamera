package com.android.tiltcamera.sensor

import android.app.Application
import android.content.pm.PackageManager
import android.hardware.Sensor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SensorModule {

    @Provides
    @Singleton
    fun provideOrientationSensor(app: Application): OrientationSensor {
        return OrientationSensor(app, PackageManager.FEATURE_SENSOR_ACCELEROMETER, Sensor.TYPE_ROTATION_VECTOR)
    }
}