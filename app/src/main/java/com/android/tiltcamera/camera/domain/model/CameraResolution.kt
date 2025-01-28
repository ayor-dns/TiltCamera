package com.android.tiltcamera.camera.domain.model

data class CameraResolution(
    val cameraResolutionId: Long,
    val displayName: String,
    val lensFacing: Int,
    val height: Int,
    val width: Int,
)
