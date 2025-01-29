package com.android.tiltcamera.camera.data.database.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PictureEntity(
    @PrimaryKey(autoGenerate = true)
    val pictureId: Long = 0,
    val creationTimestamp: Long,
    val pictureUri: Uri,
    val collectionIdFK: Long? = null,
    val azimuth: Double? = null,
    val pitch: Double? = null,
    val roll: Double? = null
)
