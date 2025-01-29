package com.android.tiltcamera.camera.domain.use_case

import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.CollectionRepository
import kotlinx.coroutines.flow.Flow

class GetPictureCollectionsUseCase(
    private val collectionRepository: CollectionRepository
) {
    operator fun invoke(): Flow<List<PicturesCollection>> {
        return collectionRepository.getPicturesCollections()
    }

}