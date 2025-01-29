package com.android.tiltcamera.core.presentation

import com.android.tiltcamera.R
import com.android.tiltcamera.core.domain.PictureCollectionNameError

fun PictureCollectionNameError.asUiText() : UiText {
    val stringRes = when(this) {
        PictureCollectionNameError.AlreadyExists -> R.string.PictureCollectionNameError_ALREADY_EXISTS
        PictureCollectionNameError.Empty -> R.string.PictureCollectionNameError_EMPTY_NAME
        PictureCollectionNameError.InvalidCharacters -> R.string.PictureCollectionNameError_INVALID_CHARACTERS
        PictureCollectionNameError.TooLong -> R.string.PictureCollectionNameError_TOO_LONG
    }
    return UiText.StringResource(stringRes)
}