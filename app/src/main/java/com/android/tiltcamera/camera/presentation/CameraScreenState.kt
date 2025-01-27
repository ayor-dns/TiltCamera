package com.android.tiltcamera.camera.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.android.tiltcamera.camera.domain.Picture

data class CameraScreenState(


    val azimuth: Double = 0.0,
    val pitch: Double = 0.0,
    val roll: Double = 0.0,

    val pictures: List<Picture> = emptyList()
)
