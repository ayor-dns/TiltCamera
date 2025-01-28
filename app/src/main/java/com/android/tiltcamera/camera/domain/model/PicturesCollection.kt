package com.android.tiltcamera.camera.domain.model

import com.android.tiltcamera.camera.domain.AspectRatioMode

data class PicturesCollection(
    val collectionId: Long = 0,
    val name: String,
    val creationTimestamp: Long,
    val cameraResolutionIdFK: Long,
    val aspectRatioMode: AspectRatioMode
)



