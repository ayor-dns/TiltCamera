package com.android.tiltcamera.camera.data.repository

import android.net.Uri
import com.android.tiltcamera.camera.data.database.dao.PictureDao
import com.android.tiltcamera.camera.data.mappers.toPicture
import com.android.tiltcamera.camera.data.mappers.toPictureEntity
import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DefaultPictureRepository(
    private val pictureDao: PictureDao
): PictureRepository {


    override suspend fun insertPicture(picture: Picture): Long {
        return pictureDao.insertPicture(picture.toPictureEntity())
    }

    override fun getPicturesByCollectionId(collectionId: Long): Flow<List<Picture>> {
        return pictureDao.getPicturesByCollectionId(collectionId).map { picturesEntities ->
            picturesEntities.map { entity -> entity.toPicture() }
        }
    }

    override suspend fun getLastPictureUriByCollectionId(collectionId: Long): Uri? {
        return pictureDao.getLastPictureUriByCollectionId(collectionId)
    }


}