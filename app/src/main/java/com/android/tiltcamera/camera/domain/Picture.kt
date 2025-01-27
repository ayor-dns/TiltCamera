package com.android.tiltcamera.camera.domain

import android.net.Uri

data class Picture(
    val pictureId: Long = 0,
    val creationTimestamp: Long,
    val pictureUri: Uri,
    val collectionIdFK: Long? = null,
    val azimuth: Float? = null,
    val pitch: Float? = null,
    val roll: Float? = null
)