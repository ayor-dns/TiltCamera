package com.android.tiltcamera.camera.domain.use_case

data class CameraUseCases(
    val savePhotoUseCase: SavePhotoUseCase,
    val getPictureCollectionUseCase: GetPictureCollectionUseCase,
    val getPictureCollectionsUseCase: GetPictureCollectionsUseCase,
    val getLastPictureUriUseCase: GetLastPictureUriUseCase,
    val insertNewCollection: InsertNewCollection,
)
