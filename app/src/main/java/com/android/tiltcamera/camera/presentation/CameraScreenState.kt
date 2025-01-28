package com.android.tiltcamera.camera.presentation

import android.net.Uri
import androidx.camera.core.CameraSelector
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.data.CameraInfoProvider
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.model.CameraResolution
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.presentation.components.OptionItem
import com.android.tiltcamera.core.presentation.UiText

data class CameraScreenState(
    val currentCollection: PicturesCollection? = null,
    val collections: List<PicturesCollection> = emptyList(),

    val currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,

    val availableResolutions: List<CameraResolution> = emptyList(),
    val currentResolution: CameraResolution? = null,

    val currentAspectRatioMode: AspectRatioMode = AspectRatioMode.RATIO_4_3,
    val aspectRatioOptions: List<OptionItem> = listOf(
        OptionItem(id = 0, icon = R.drawable.format_16_9, data = AspectRatioMode.RATIO_16_9),
        OptionItem(id = 1, icon = R.drawable.format_4_3, data = AspectRatioMode.RATIO_4_3),
    ),

    val cameraInfos: List<CameraInfoProvider.CameraInfo> = emptyList(),


    val azimuth: Double = 0.0,
    val pitch: Double = 0.0,
    val roll: Double = 0.0,

    val isSavingPicture: Boolean = false,
    val showPictureInfo: Boolean = true,
    val hasFrontCamera: Boolean = false,
    val lastPictureUri: Uri? = null,
    val showNewCollectionDialog: Boolean = false,

    // NEW COLLECTIONS
    val newCollection: PicturesCollection = PicturesCollection.newCollection(),
    val nameError: UiText? = null,

    )

