package com.android.tiltcamera.camera.presentation

import android.graphics.Bitmap


sealed interface CameraAction {
    data class OnTakePhoto(val bitmap: Bitmap): CameraAction
}