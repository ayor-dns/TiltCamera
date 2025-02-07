package com.android.tiltcamera.gallery.presentation.collection_gallery

import com.android.tiltcamera.camera.domain.model.PicturesCollection

sealed interface GalleryAction {
    data class OnSearchQueryChange(val query: String): GalleryAction
    data class OnCollectionClick(val collection: PicturesCollection): GalleryAction
    data object OnBackClick: GalleryAction

}