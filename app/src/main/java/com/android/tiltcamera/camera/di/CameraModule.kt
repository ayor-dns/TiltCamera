package com.android.tiltcamera.camera.di

import android.content.Context
import androidx.room.Room
import com.android.tiltcamera.camera.data.CameraInfoProvider
import com.android.tiltcamera.camera.data.database.PictureDatabase
import com.android.tiltcamera.camera.data.repository.DefaultCollectionRepository
import com.android.tiltcamera.camera.data.repository.DefaultPictureRepository
import com.android.tiltcamera.camera.data.repository.DefaultPreferencesRepository
import com.android.tiltcamera.camera.domain.PicturesCollectionValidator
import com.android.tiltcamera.camera.domain.repository.CollectionRepository
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import com.android.tiltcamera.camera.domain.repository.PreferencesRepository
import com.android.tiltcamera.camera.domain.use_case.CameraUseCases
import com.android.tiltcamera.camera.domain.use_case.GetLastPictureUriUseCase
import com.android.tiltcamera.camera.domain.use_case.GetPictureCollectionUseCase
import com.android.tiltcamera.camera.domain.use_case.GetPictureCollectionsUseCase
import com.android.tiltcamera.camera.domain.use_case.InsertNewCollection
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

    @Singleton
    @Provides
    fun provideCollectionDao(db: PictureDatabase) = db.collectionDao

    @Provides
    @Singleton
    fun provideCameraUseCases(
        @ApplicationContext context: Context,
        pictureRepository: PictureRepository,
        collectionRepository: CollectionRepository
    ): CameraUseCases {
        return CameraUseCases(
            savePhotoUseCase = SavePhotoUseCase(context, pictureRepository),
            getPictureCollectionUseCase = GetPictureCollectionUseCase(collectionRepository),
            getPictureCollectionsUseCase = GetPictureCollectionsUseCase(collectionRepository),
            getLastPictureUriUseCase = GetLastPictureUriUseCase(pictureRepository),
            insertNewCollection = InsertNewCollection(collectionRepository)
        )
    }

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

    @Provides
    @Singleton
    fun providePreferenceRepository(
        @ApplicationContext context: Context,
    ): PreferencesRepository {
        return DefaultPreferencesRepository(context)
    }

    @Provides
    @Singleton
    fun provideCameraInfoProvider(@ApplicationContext context: Context) : CameraInfoProvider {
        return CameraInfoProvider(context)

    }

    @Provides
    @Singleton
    fun providePictureCollectionValidator(
        collectionRepository: CollectionRepository
    ): PicturesCollectionValidator {
        return PicturesCollectionValidator(collectionRepository)

    }


}