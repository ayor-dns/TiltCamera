package com.android.tiltcamera.camera.domain.model

import android.net.Uri

data class Picture(
    val pictureId: Long = 0,
    val creationTimestamp: Long,
    val pictureName: String,
    val pictureUri: Uri,
    val width: Int,
    val height: Int,
    val collectionIdFK: Long? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val azimuth: Double? = null,
    val pitch: Double? = null,
    val roll: Double? = null
) {
    companion object {
        fun empty(): Picture {
            return Picture(
                creationTimestamp = 0,
                pictureName = "",
                pictureUri = Uri.EMPTY,
                width = 0,
                height = 0
            )
        }
    }
}