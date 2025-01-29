package com.android.tiltcamera.camera.domain.use_case

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.CollectionRepository

class GetPictureCollectionUseCase(
    private val collectionRepository: CollectionRepository
) {
    suspend operator fun invoke(pictureCollectionId: Long): PicturesCollection {
        return collectionRepository.getPicturesCollectionById(pictureCollectionId)
    }
}