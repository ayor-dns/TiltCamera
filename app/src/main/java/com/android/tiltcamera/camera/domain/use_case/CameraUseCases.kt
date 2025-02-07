package com.android.tiltcamera.camera.domain.use_case

data class CameraUseCases(
    val savePhotoUseCase: SavePhotoUseCase,
    val getPictureCollectionUseCase: GetPictureCollectionUseCase,
    val getActivePictureCollectionsUseCase: GetActivePictureCollectionsUseCase,
    val getLastPictureUseCase: GetLastPictureUseCase,
    val insertNewCollection: InsertNewCollection,
)
