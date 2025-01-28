package com.android.tiltcamera.camera.domain.model

import com.android.tiltcamera.camera.domain.AspectRatioMode

data class PicturesCollection(
    val collectionId: Long = 0,
    val name: String,
    val creationTimestamp: Long,
    val cameraResolutionIdFK: Long,
    val aspectRatioMode: AspectRatioMode
) {
    companion object {
        fun newCollection(): PicturesCollection {
            return PicturesCollection(
                collectionId = -1,
                name = "Nouvelle collection",
                creationTimestamp = 0,
                cameraResolutionIdFK = -1,
                aspectRatioMode = AspectRatioMode.RATIO_4_3
            )
        }
    }
}



