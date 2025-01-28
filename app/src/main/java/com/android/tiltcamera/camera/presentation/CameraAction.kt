package com.android.tiltcamera.camera.presentation

import android.graphics.Bitmap
import androidx.camera.core.CameraSelector
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.model.CameraResolution
import com.android.tiltcamera.camera.domain.model.PicturesCollection


sealed interface CameraAction {
    data class OnTakePhoto(val bitmap: Bitmap): CameraAction
    data class SetAspectRatioMode(val aspectRatioMode: AspectRatioMode): CameraAction
    data class OnPictureCollectionSelected(val picturesCollection: PicturesCollection): CameraAction
    data class OnCameraResolutionSelected(val cameraResolution: CameraResolution): CameraAction
    data class SetShowPictureInfo(val showPictureInfo: Boolean): CameraAction
    data object ShowNewCollectionDialog: CameraAction
    data class SwitchCamera(val cameraSelector: CameraSelector): CameraAction

    data class SetNewCollectionName(val name: String): CameraAction
    data class SetNewCollectionAspectRatioMode(val aspectRatioMode: AspectRatioMode): CameraAction
    data object CancelNewCollectionDialog: CameraAction
    data object ConfirmNewCollectionDialog: CameraAction
    data object DismissNewCollectionDialog: CameraAction





}