package com.android.tiltcamera.camera.data.mappers

import com.android.tiltcamera.camera.data.database.entity.CameraResolutionEntity
import com.android.tiltcamera.camera.domain.model.CameraResolution

fun CameraResolution.toCameraResolutionEntity(): CameraResolutionEntity {
    return CameraResolutionEntity(
        cameraResolutionId = cameraResolutionId,
        displayName = displayName,
        lensFacing = lensFacing,
        height = height,
        width = width
    )
}

fun CameraResolutionEntity.toCameraResolution(): CameraResolution {
    return CameraResolution(
        cameraResolutionId = cameraResolutionId,
        displayName = displayName,
        lensFacing = lensFacing,
        height = height,
        width = width
    )
}