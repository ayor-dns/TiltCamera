package com.android.tiltcamera.camera.domain

import com.android.tiltcamera.core.domain.repository.CollectionRepository
import com.android.tiltcamera.core.domain.PictureCollectionNameError
import com.android.tiltcamera.core.domain.Result

class PicturesCollectionValidator(
    private val collectionRepository: CollectionRepository
) {

    suspend fun validateName(name: String): Result<Unit, PictureCollectionNameError> {
        if(name.isBlank()) return Result.Error(PictureCollectionNameError.Empty)

        if(name.length > 100) return Result.Error(PictureCollectionNameError.TooLong)

        if(!name.matches(Regex("^[a-zA-Z0-9_\\-/ ]+$"))) return Result.Error(PictureCollectionNameError.InvalidCharacters)

        val collection = collectionRepository.getPictureCollectionByName(name)
        if(collection != null) return Result.Error(PictureCollectionNameError.AlreadyExists)

        return Result.Success(Unit)
    }
}