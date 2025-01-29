package com.android.tiltcamera.camera.data.repository

import android.net.Uri
import com.android.tiltcamera.camera.data.database.dao.PictureDao
import com.android.tiltcamera.camera.domain.repository.PictureRepository

class DefaultPictureRepository(
    private val pictureDao: PictureDao
): PictureRepository {


    override suspend fun getLastPictureUriByCollectionId(collectionId: Long): Uri? {
        return pictureDao.getLastPictureUriByCollectionId(collectionId)
    }


}