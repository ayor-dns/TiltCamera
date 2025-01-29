package com.android.tiltcamera.camera.domain.repository

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.core.domain.Result
import com.android.tiltcamera.core.domain.RoomError
import kotlinx.coroutines.flow.Flow

interface CollectionRepository {

    suspend fun insertPicturesCollection(collection: PicturesCollection): Result<Long, RoomError>
    suspend fun getPicturesCollectionById(id: Long): PicturesCollection
    fun getPicturesCollections(): Flow<List<PicturesCollection>>
    suspend fun getPictureCollectionByName(name: String): PicturesCollection?
}