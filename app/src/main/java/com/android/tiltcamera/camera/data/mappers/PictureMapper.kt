package com.android.tiltcamera.camera.data.mappers

import com.android.tiltcamera.camera.data.database.entity.PictureEntity
import com.android.tiltcamera.camera.domain.model.Picture

fun Picture.toPictureEntity(): PictureEntity {
    return PictureEntity(
        pictureId = pictureId,
        creationTimestamp = creationTimestamp,
        pictureName = pictureName,
        pictureUri = pictureUri,
        width = width,
        height = height,
        collectionIdFK = collectionIdFK,
        latitude = latitude,
        longitude = longitude,
        azimuth = azimuth,
        pitch = pitch,
        roll = roll
    )
}

fun PictureEntity.toPicture(): Picture {
    return Picture(
        pictureId = pictureId,
        creationTimestamp = creationTimestamp,
        pictureName = pictureName,
        pictureUri = pictureUri,
        width = width,
        height = height,
        collectionIdFK = collectionIdFK,
        latitude = latitude,
        longitude = longitude,
        azimuth = azimuth,
        pitch = pitch,
        roll = roll
    )
}