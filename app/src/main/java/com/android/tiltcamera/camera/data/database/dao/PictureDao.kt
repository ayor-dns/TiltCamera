package com.android.tiltcamera.camera.data.database.dao

import android.net.Uri
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

    @Query("SELECT pictureUri FROM PictureEntity WHERE collectionIdFK = :collectionId ORDER BY creationTimestamp DESC LIMIT 1")
    suspend fun getLastPictureUriByCollectionId(collectionId: Long): Uri?

}