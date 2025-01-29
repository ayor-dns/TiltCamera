package com.android.tiltcamera.camera.domain.model

import android.net.Uri

data class Picture(
    val pictureId: Long = 0,
    val creationTimestamp: Long,
    val pictureUri: Uri,
    val collectionIdFK: Long? = null,
    val azimuth: Double? = null,
    val pitch: Double? = null,
    val roll: Double? = null
)