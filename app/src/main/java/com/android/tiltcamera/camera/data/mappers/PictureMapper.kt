package com.android.tiltcamera.camera.data.mappers

import com.android.tiltcamera.camera.data.database.entity.PictureEntity
import com.android.tiltcamera.camera.domain.model.Picture

fun Picture.toPictureEntity(): PictureEntity {
    return PictureEntity(
        pictureId = pictureId,
        creationTimestamp = creationTimestamp,
        pictureUri = pictureUri,
        collectionIdFK = collectionIdFK,
        azimuth = azimuth,
        pitch = pitch,
        roll = roll
    )
}

fun PictureEntity.toPicture(): Picture {
    return Picture(
        pictureId = pictureId,
        creationTimestamp = creationTimestamp,
        pictureUri = pictureUri,
        collectionIdFK = collectionIdFK,
        azimuth = azimuth,
        pitch = pitch,
        roll = roll
    )
}