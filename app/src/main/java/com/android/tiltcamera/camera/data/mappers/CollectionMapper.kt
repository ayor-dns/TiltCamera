package com.android.tiltcamera.camera.data.mappers

import com.android.tiltcamera.camera.data.database.entity.PictureCollectionEntity
import com.android.tiltcamera.camera.domain.model.PicturesCollection

fun PicturesCollection.toPicturesCollectionEntity(): PictureCollectionEntity {
    return PictureCollectionEntity(
        collectionId = collectionId,
        collectionName = collectionName,
        creationTimestamp = creationTimestamp,
        cameraResolution = cameraResolution,
        aspectRatioMode = aspectRatioMode,
        lensFacing = lensFacing,
        isActive = isActive
    )
}

fun PictureCollectionEntity.toPicturesCollection(): PicturesCollection {
    return PicturesCollection(
        collectionId = collectionId,
        collectionName = collectionName,
        creationTimestamp = creationTimestamp,
        cameraResolution = cameraResolution,
        aspectRatioMode = aspectRatioMode,
        lensFacing = lensFacing,
        isActive = isActive
    )
}