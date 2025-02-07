package com.android.tiltcamera.gallery.presentation.picture_detail

import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.camera.domain.model.PicturesCollection

data class PictureDetailState(
    val isLoading: Boolean = false,
    val picture: Picture? = null,
    val collection: PicturesCollection? = null,
)