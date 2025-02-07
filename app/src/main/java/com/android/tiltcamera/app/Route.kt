package com.android.tiltcamera.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object CameraGraph: Route

    @Serializable
    data object CameraScreen: Route

    @Serializable
    data object GalleryScreen: Route

    @Serializable
    data class GalleryDetailScreen(val collectionId: Long): Route

    @Serializable
    data class PictureDetailScreen(val pictureId: Long): Route

}