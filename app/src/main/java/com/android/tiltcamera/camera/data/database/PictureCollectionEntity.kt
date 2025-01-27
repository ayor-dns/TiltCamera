package com.android.tiltcamera.camera.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PictureCollectionEntity(
    @PrimaryKey(autoGenerate = true)
    val collectionId: Long = 0,
    val name: String,
    val creationTimestamp: Long
)
