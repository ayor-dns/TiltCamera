package com.android.tiltcamera.camera.domain.use_case

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.core.domain.repository.CollectionRepository
import com.android.tiltcamera.core.domain.Result
import com.android.tiltcamera.core.domain.RoomError
import com.android.tiltcamera.core.domain.getCurrentTimeInMillis

class InsertNewCollection(
    private val pictureCollectionRepository: CollectionRepository
) {
    suspend operator fun invoke(collection: PicturesCollection): Result<Long, RoomError> {
        return pictureCollectionRepository.insertPicturesCollection(collection.copy(creationTimestamp = getCurrentTimeInMillis()))
    }

}