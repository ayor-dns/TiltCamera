package com.android.tiltcamera.camera.data.database.entity

import android.util.Size
import androidx.camera.core.CameraSelector.LensFacing
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.tiltcamera.camera.domain.AspectRatioMode

@Entity
data class PictureCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val collectionId: Long = 0,
    val name: String,
    val creationTimestamp: Long,
    val cameraResolution: Size,
    val aspectRatioMode: AspectRatioMode,
    val lensFacing: Int,
)
