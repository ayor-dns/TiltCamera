package com.android.tiltcamera.camera.domain.use_case

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.PictureRepository

class GetPictureCollectionUseCase(
    private val pictureRepository: PictureRepository
) {
    suspend operator fun invoke(pictureCollectionId: Long): PicturesCollection {
        return pictureRepository.getPicturesCollectionById(pictureCollectionId)
    }
}