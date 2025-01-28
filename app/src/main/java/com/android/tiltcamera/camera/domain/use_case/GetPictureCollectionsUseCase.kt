package com.android.tiltcamera.camera.domain.use_case

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow

class GetPictureCollectionsUseCase(
    private val pictureRepository: PictureRepository
) {
    operator fun invoke(): Flow<List<PicturesCollection>> {
        return pictureRepository.getPicturesCollections()
    }

}