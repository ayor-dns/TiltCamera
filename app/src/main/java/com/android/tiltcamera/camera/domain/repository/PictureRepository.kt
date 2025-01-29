package com.android.tiltcamera.camera.domain.repository

import android.net.Uri
import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import kotlinx.coroutines.flow.Flow

interface PictureRepository {

    suspend fun insertPicture(picture: Picture): Long
    fun getPicturesByCollectionId(collectionId: Long): Flow<List<Picture>>
    suspend fun getLastPictureUriByCollectionId(collectionId: Long): Uri?

}