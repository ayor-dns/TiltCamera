package com.android.tiltcamera.core.domain

sealed interface PictureCollectionNameError: Error {
    data object Empty: PictureCollectionNameError
    data object TooLong: PictureCollectionNameError
    data object InvalidCharacters: PictureCollectionNameError
    data object AlreadyExists: PictureCollectionNameError

}