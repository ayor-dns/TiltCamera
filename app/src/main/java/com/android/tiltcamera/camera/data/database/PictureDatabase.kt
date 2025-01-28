package com.android.tiltcamera.camera.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.android.tiltcamera.camera.data.database.entity.PictureCollectionEntity
import com.android.tiltcamera.camera.data.database.entity.PictureEntity

@Database(
    entities = [
        PictureCollectionEntity::class,
        PictureEntity::class
    ],
    version = 1
)
@TypeConverters(UriConverters::class)
abstract class PictureDatabase: RoomDatabase() {

    abstract val pictureDao: PictureDao

    companion object {
        const val DATABASE_NAME = "picture_db"
    }
}