package com.android.tiltcamera.camera.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.tiltcamera.camera.data.database.entity.PictureCollectionEntity
import com.android.tiltcamera.camera.data.database.entity.PictureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity)

    @Delete
    suspend fun deletePicture(picture: PictureEntity)

    @Query("SELECT * FROM PictureEntity")
    fun getPictures(): Flow<List<PictureEntity>>

    @Query("SELECT * FROM PictureCollectionEntity WHERE collectionId = :id")
    suspend fun getPicturesCollectionById(id: Long): PictureCollectionEntity

    @Query("SELECT * FROM PictureCollectionEntity")
    fun getPicturesByCollections(): Flow<List<PictureCollectionEntity>>
}