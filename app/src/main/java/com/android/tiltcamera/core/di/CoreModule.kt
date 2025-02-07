package com.android.tiltcamera.core.di

import android.content.Context
import androidx.room.Room
import com.android.tiltcamera.camera.data.database.PictureDatabase
import com.android.tiltcamera.core.data.repository.DefaultCollectionRepository
import com.android.tiltcamera.core.data.repository.DefaultPictureRepository
import com.android.tiltcamera.core.domain.repository.CollectionRepository
import com.android.tiltcamera.core.domain.repository.PictureRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CoreModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext app: Context,
    ) = Room.databaseBuilder(
        app,
        PictureDatabase::class.java,
        PictureDatabase.DATABASE_NAME
    )
        .build()


    @Singleton
    @Provides
    fun providePictureDao(db: PictureDatabase) = db.pictureDao

    @Singleton
    @Provides
    fun provideCollectionDao(db: PictureDatabase) = db.collectionDao

    @Provides
    @Singleton
    fun providePictureRepository(db: PictureDatabase): PictureRepository {
        return DefaultPictureRepository(db.pictureDao)
    }

    @Provides
    @Singleton
    fun provideCollectionRepository(db: PictureDatabase): CollectionRepository {
        return DefaultCollectionRepository(db.collectionDao)
    }
}