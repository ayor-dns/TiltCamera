package com.android.tiltcamera.camera.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.tiltcamera.camera.data.database.entity.PictureEntity
import com.android.tiltcamera.camera.domain.model.Picture
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPicture(picture: PictureEntity): Long

    @Delete
    suspend fun deletePicture(picture: PictureEntity): Int

    @Query("SELECT * FROM PictureEntity WHERE pictureId = :id")
    fun getPictureByIdAsFlow(id: Long?): Flow<Picture?>

    @Query("SELECT * FROM PictureEntity")
    fun getPictures(): Flow<List<PictureEntity>>

    @Query("SELECT * FROM PictureEntity WHERE collectionIdFK = :collectionId")
    fun getPicturesByCollectionId(collectionId: Long): Flow<List<PictureEntity>>

    @Query("SELECT * FROM PictureEntity WHERE collectionIdFK = :collectionId ORDER BY creationTimestamp DESC LIMIT 1")
    fun getLastPictureUriByCollectionId(collectionId: Long): Flow<Picture?>

}