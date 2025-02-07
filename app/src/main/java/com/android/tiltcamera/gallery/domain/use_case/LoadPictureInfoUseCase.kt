package com.android.tiltcamera.gallery.domain.use_case

import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.core.domain.repository.PictureRepository
import kotlinx.coroutines.flow.Flow

class LoadPictureInfoUseCase(
    private val pictureRepository: PictureRepository
) {

    operator fun invoke(pictureId: Long?): Flow<Picture?> {
        return pictureRepository.getPictureByIdAsFlow(pictureId)
    }
}