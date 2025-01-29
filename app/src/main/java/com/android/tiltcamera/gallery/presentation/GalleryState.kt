package com.android.tiltcamera.gallery.presentation

import com.android.tiltcamera.camera.domain.model.PicturesCollection

data class GalleryState(
    val picturesCollections: List<PicturesCollection> = emptyList(),
)