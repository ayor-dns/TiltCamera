package com.android.tiltcamera.camera.domain.repository

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import kotlinx.coroutines.flow.Flow

interface PictureRepository {

    suspend fun getPicturesCollectionById(id: Long): PicturesCollection
    fun getPicturesCollections(): Flow<List<PicturesCollection>>
}