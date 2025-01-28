package com.android.tiltcamera.camera.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CameraResolutionEntity(
    @PrimaryKey(autoGenerate = true)
    val cameraResolutionId: Long,
    val displayName: String,
    val height: Long,
    val width: Long,
)
