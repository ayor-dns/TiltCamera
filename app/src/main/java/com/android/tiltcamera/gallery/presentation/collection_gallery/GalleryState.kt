package com.android.tiltcamera.gallery.presentation.collection_gallery

import com.android.tiltcamera.core.presentation.UiText

data class GalleryState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val errorMessage: UiText? = null
)