package com.android.tiltcamera.core.domain

sealed interface CameraError: Error {

    data object CameraAccessError : CameraError
    data object UnknownError : CameraError

}