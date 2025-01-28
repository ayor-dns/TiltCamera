package com.android.tiltcamera.camera.data.repository

import com.android.tiltcamera.camera.data.database.PictureDao
import com.android.tiltcamera.camera.data.mappers.toPicturesCollection
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultPictureRepository(
    private val pictureDao: PictureDao
): PictureRepository {
    override suspend fun getPicturesCollectionById(id: Long): PicturesCollection {
        return pictureDao.getPicturesCollectionById(id).toPicturesCollection()
    }

    override fun getPicturesCollections(): Flow<List<PicturesCollection>> {
        return pictureDao.getPicturesByCollections().map {entityList ->
            entityList.map { entity ->
                entity.toPicturesCollection()
            }
        }
    }
}