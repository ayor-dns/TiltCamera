package com.android.tiltcamera.camera.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.tiltcamera.camera.data.database.entity.PictureCollectionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionDao {

    @Insert
    suspend fun insertPicturesCollection(collection: PictureCollectionEntity): Long

    @Query("SELECT * FROM PictureCollectionEntity WHERE collectionId = :id")
    suspend fun getPicturesCollectionById(id: Long): PictureCollectionEntity

    @Query("SELECT * FROM PictureCollectionEntity WHERE isActive = 1")
    fun getActivePicturesByCollections(): Flow<List<PictureCollectionEntity>>

    @Query("SELECT * FROM PictureCollectionEntity WHERE collectionName = :name")
    suspend fun getPictureCollectionByName(name: String): PictureCollectionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCollection(collection: PictureCollectionEntity): Long
}