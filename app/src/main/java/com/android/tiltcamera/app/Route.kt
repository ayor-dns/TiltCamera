package com.android.tiltcamera.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object CameraGraph: Route

    @Serializable
    data object CameraScreen: Route

    @Serializable
    data object GalleryScreen: Route
}