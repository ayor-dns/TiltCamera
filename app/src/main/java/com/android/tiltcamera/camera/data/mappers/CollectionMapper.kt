package com.android.tiltcamera.camera.data.mappers

import com.android.tiltcamera.camera.data.database.entity.PictureCollectionEntity
import com.android.tiltcamera.camera.domain.model.PicturesCollection

fun PicturesCollection.toPicturesCollectionEntity(): PictureCollectionEntity {
    return PictureCollectionEntity(
        collectionId = collectionId,
        name = name,
        creationTimestamp = creationTimestamp,
        cameraResolution = cameraResolution,
        aspectRatioMode = aspectRatioMode,
        lensFacing = lensFacing
    )
}

fun PictureCollectionEntity.toPicturesCollection(): PicturesCollection {
    return PicturesCollection(
        collectionId = collectionId,
        name = name,
        creationTimestamp = creationTimestamp,
        cameraResolution = cameraResolution,
        aspectRatioMode = aspectRatioMode,
        lensFacing = lensFacing
    )
}