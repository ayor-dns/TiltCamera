package com.android.tiltcamera.gallery.presentation.picture_detail

sealed interface PictureDetailAction {
    data object OnBackClick : PictureDetailAction
    data object OnDeleteClick : PictureDetailAction

}