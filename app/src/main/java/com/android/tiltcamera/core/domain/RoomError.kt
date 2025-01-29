package com.android.tiltcamera.core.domain

sealed interface RoomError: Error {
    data object InsertError: RoomError
}