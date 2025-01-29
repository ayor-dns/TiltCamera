package com.android.tiltcamera.camera.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.tiltcamera.camera.data.database.dao.CollectionDao
import com.android.tiltcamera.camera.data.database.dao.PictureDao
import com.android.tiltcamera.camera.data.database.entity.PictureCollectionEntity
import com.android.tiltcamera.camera.data.database.entity.PictureEntity
import com.android.tiltcamera.camera.data.database.typeConverter.SizeConverter
import com.android.tiltcamera.camera.data.database.typeConverter.UriConverters

@Database(
    entities = [
        PictureCollectionEntity::class,
        PictureEntity::class
    ],
    version = 1
)
@TypeConverters(UriConverters::class, SizeConverter::class)
abstract class PictureDatabase: RoomDatabase() {

    abstract val pictureDao: PictureDao
    abstract val collectionDao: CollectionDao

    companion object {
        const val DATABASE_NAME = "picture_db"
    }
}