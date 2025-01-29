package com.android.tiltcamera.camera.domain.use_case

import android.net.Uri
import com.android.tiltcamera.camera.domain.repository.PictureRepository

class GetLastPictureUriUseCase(
    private val pictureRepository: PictureRepository

) {
    suspend operator fun invoke(collectionId: Long): Uri? {
        return pictureRepository.getLastPictureUriByCollectionId(collectionId)
    }
}