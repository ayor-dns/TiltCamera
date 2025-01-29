package com.android.tiltcamera.camera.domain.model

import android.util.Size
import androidx.camera.core.CameraSelector
import com.android.tiltcamera.camera.domain.AspectRatioMode

data class PicturesCollection(
    val collectionId: Long = 0,
    val name: String,
    val creationTimestamp: Long,
    val cameraResolution: Size,
    val aspectRatioMode: AspectRatioMode,
    val lensFacing: Int,
) {
    companion object {
        fun newCollection(): PicturesCollection {
            return PicturesCollection(
                collectionId = 0,
                name = "Nouvelle collection",
                creationTimestamp = 0,
                cameraResolution = Size(Int.MAX_VALUE, Int.MAX_VALUE),
                aspectRatioMode = AspectRatioMode.RATIO_4_3,
                lensFacing = CameraSelector.LENS_FACING_BACK
            )
        }
    }
}



