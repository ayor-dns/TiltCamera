package com.android.tiltcamera.camera.data.database.entity

import android.util.Size
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.tiltcamera.camera.domain.AspectRatioMode

@Entity
data class PictureCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val collectionId: Long = 0,
    val collectionName: String,
    val creationTimestamp: Long,
    val cameraResolution: Size,
    val aspectRatioMode: AspectRatioMode,
    val lensFacing: Int,
    val isActive: Boolean = true,
)
