package com.android.tiltcamera.camera.presentation

import android.net.Uri
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.presentation.components.OptionItem

data class CameraScreenState(
    val currentCollection: PicturesCollection? = null,
    val collections: List<PicturesCollection> = emptyList(),
    val pictures: List<Picture> = emptyList(),

    val azimuth: Double = 0.0,
    val pitch: Double = 0.0,
    val roll: Double = 0.0,

    val isSavingPicture: Boolean = false,
    val showPictureInfo: Boolean = true,
    val hasFrontCamera: Boolean = false,
    val lastPictureUri: Uri? = null,

    val aspectRatioMode: AspectRatioMode = AspectRatioMode.RATIO_4_3,
    val aspectRatioOptions: List<OptionItem> = listOf(
        OptionItem(id = 0, icon = R.drawable.format_16_9, data = AspectRatioMode.RATIO_16_9),
        OptionItem(id = 1, icon = R.drawable.format_4_3, data = AspectRatioMode.RATIO_4_3),
        ),


    )

