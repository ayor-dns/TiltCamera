package com.android.tiltcamera.core.data.repository

import com.android.tiltcamera.camera.data.database.dao.PictureDao
import com.android.tiltcamera.camera.data.mappers.toPicture
import com.android.tiltcamera.camera.data.mappers.toPictureEntity
import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.core.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultPictureRepository(
    private val pictureDao: PictureDao
): PictureRepository {
    override fun getPictureByIdAsFlow(pictureId: Long?): Flow<Picture?> {
        return pictureDao.getPictureByIdAsFlow(pictureId)
    }


    override suspend fun insertPicture(picture: Picture): Long {
        return pictureDao.insertPicture(picture.toPictureEntity())
    }

    override fun getPicturesByCollectionId(collectionId: Long): Flow<List<Picture>> {
        return pictureDao.getPicturesByCollectionId(collectionId).map { picturesEntities ->
            picturesEntities.map { entity -> entity.toPicture() }
        }
    }

    override fun getLastPictureUriByCollectionId(collectionId: Long): Flow<Picture?> {
        return pictureDao.getLastPictureUriByCollectionId(collectionId)
    }


}