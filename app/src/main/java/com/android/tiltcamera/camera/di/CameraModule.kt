package com.android.tiltcamera.camera.di

import android.content.Context
import androidx.room.Room
import com.android.tiltcamera.camera.data.database.PictureDatabase
import com.android.tiltcamera.camera.domain.use_case.CameraUseCases
import com.android.tiltcamera.camera.domain.use_case.SavePhotoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CameraModule {

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

    @Provides
    @Singleton
    fun provideCameraUseCases(
        @ApplicationContext context: Context,
    ): CameraUseCases {
        return CameraUseCases(
            savePhotoUseCase = SavePhotoUseCase(context)
        )
    }
}