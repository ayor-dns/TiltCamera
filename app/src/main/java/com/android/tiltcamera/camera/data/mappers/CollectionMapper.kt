package com.android.tiltcamera.camera.data.mappers

import com.android.tiltcamera.camera.data.database.PictureCollectionEntity
import com.android.tiltcamera.camera.domain.PicturesCollection

fun PicturesCollection.toPicturesCollectionEntity(): PictureCollectionEntity {
    return PictureCollectionEntity(
        collectionId = collectionId,
        name = name,
        creationTimestamp = creationTimestamp
    )
}

fun PictureCollectionEntity.toPicturesCollection(): PicturesCollection {
    return PicturesCollection(
        collectionId = collectionId,
        name = name,
        creationTimestamp = creationTimestamp,
    )
}