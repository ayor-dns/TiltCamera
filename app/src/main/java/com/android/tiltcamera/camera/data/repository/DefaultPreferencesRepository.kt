package com.android.tiltcamera.camera.data.repository

import android.content.Context
import com.android.tiltcamera.camera.domain.repository.PreferencesRepository
import javax.inject.Inject

class DefaultPreferencesRepository @Inject constructor(
    context: Context
): PreferencesRepository {

    companion object {
        private const val SHARED_PREFERENCES_NAMES = "my_prefs"
        private const val CURRENT_PICTURE_COLLECTION_ID = "CURRENT_PICTURE_COLLECTION_ID"
        private const val SHOULD_SHOW_PICTURE_INFO = "SHOULD_SHOW_PICTURE_INFO"
    }

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_NAMES, Context.MODE_PRIVATE)


    override fun getCurrentPictureCollectionId(): Long? {
        val id = sharedPreferences.getLong(CURRENT_PICTURE_COLLECTION_ID, -1)
        return if(id == -1L) null else id
    }

    override fun setCurrentPictureCollectionId(id: Long?) {
        sharedPreferences.edit().putLong(CURRENT_PICTURE_COLLECTION_ID, id ?: -1).apply()
    }

    override fun getShowPictureInfoPreference(): Boolean {
        return sharedPreferences.getBoolean(SHOULD_SHOW_PICTURE_INFO, true)
    }

    override fun setShowPictureInfoPreference(showPictureInfo: Boolean) {
        sharedPreferences.edit().putBoolean(SHOULD_SHOW_PICTURE_INFO, showPictureInfo).apply()
    }

}