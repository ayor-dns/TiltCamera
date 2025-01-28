package com.android.tiltcamera.camera.presentation

import android.graphics.Bitmap
import com.android.tiltcamera.camera.domain.CameraResolution
import com.android.tiltcamera.camera.domain.PicturesCollection


sealed interface CameraAction {
    data class OnTakePhoto(val bitmap: Bitmap): CameraAction
    data class SetAspectRatioMode(val aspectRatioMode: AspectRatioMode): CameraAction
    data class OnPictureCollectionSelected(val pictureCollection: PicturesCollection): CameraAction
    data class OnCameraResolutionSelected(val cameraResolution: CameraResolution): CameraAction
    data class SetShowPictureInfo(val showPictureInfo: Boolean): CameraAction


}