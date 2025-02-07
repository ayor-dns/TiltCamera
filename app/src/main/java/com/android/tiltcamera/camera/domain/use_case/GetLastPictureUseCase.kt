package com.android.tiltcamera.camera.domain.use_case

import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.core.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow

class GetLastPictureUseCase(
    private val pictureRepository: PictureRepository

) {
    operator fun invoke(collectionId: Long): Flow<Picture?> {
        return pictureRepository.getLastPictureUriByCollectionId(collectionId)
    }
}