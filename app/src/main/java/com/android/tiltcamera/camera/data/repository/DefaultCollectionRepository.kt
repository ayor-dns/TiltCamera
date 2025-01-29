package com.android.tiltcamera.camera.data.repository

import com.android.tiltcamera.camera.data.database.dao.CollectionDao
import com.android.tiltcamera.camera.data.mappers.toPicturesCollection
import com.android.tiltcamera.camera.data.mappers.toPicturesCollectionEntity
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.CollectionRepository
import com.android.tiltcamera.core.domain.Result
import com.android.tiltcamera.core.domain.RoomError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber

class DefaultCollectionRepository(
    private val collectionDao: CollectionDao
): CollectionRepository {

    override suspend fun getPicturesCollectionById(id: Long): PicturesCollection {
        return collectionDao.getPicturesCollectionById(id).toPicturesCollection()
    }

    override fun getPicturesCollections(): Flow<List<PicturesCollection>> {
        return collectionDao.getPicturesByCollections().map {entityList ->
            entityList.map { entity ->
                entity.toPicturesCollection()
            }
        }
    }

    override suspend fun getPictureCollectionByName(name: String): PicturesCollection? {
        return collectionDao.getPictureCollectionByName(name)?.toPicturesCollection()
    }


    override suspend fun insertPicturesCollection(collection: PicturesCollection): Result<Long, RoomError> {
        try {
            val id = collectionDao.insertPicturesCollection(collection.toPicturesCollectionEntity())
            return Result.Success(id)
        } catch (e: Exception) {
            Timber.e(e, "Error inserting collection")
            return Result.Error(RoomError.InsertError)

        }
    }

}