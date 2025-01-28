package com.android.tiltcamera.camera.domain.repository

interface PreferencesRepository {

    fun getCurrentPictureCollectionId(): Long?
    fun setCurrentPictureCollectionId(id: Long?)

    fun getShowPictureInfoPreference(): Boolean
    fun setShowPictureInfoPreference(showPictureInfo : Boolean)

}