package com.android.tiltcamera.core.domain.repository

import com.android.tiltcamera.camera.domain.model.Picture
import kotlinx.coroutines.flow.Flow

interface PictureRepository {

    fun getPictureByIdAsFlow(pictureId: Long?): Flow<Picture?>
    suspend fun insertPicture(picture: Picture): Long
    fun getPicturesByCollectionId(collectionId: Long): Flow<List<Picture>>
    fun getLastPictureUriByCollectionId(collectionId: Long): Flow<Picture?>

}