package com.android.tiltcamera.camera.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity)

    @Delete
    suspend fun deletePicture(picture: PictureEntity)

    @Query("SELECT * FROM PictureEntity")
    fun getPictures(): Flow<List<PictureEntity>>

    @Query("SELECT * FROM PictureEntity WHERE collectionIdFK = :collectionId")
    fun getPicturesByCollectionId(collectionId: Long): Flow<List<PictureEntity>>
}