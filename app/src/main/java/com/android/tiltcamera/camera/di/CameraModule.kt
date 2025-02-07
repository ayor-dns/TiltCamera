package com.android.tiltcamera.camera.di

import android.content.Context
import com.android.tiltcamera.camera.data.CameraInfoProvider
import com.android.tiltcamera.camera.data.repository.DefaultPreferencesRepository
import com.android.tiltcamera.camera.domain.PicturesCollectionValidator
import com.android.tiltcamera.camera.domain.repository.PreferencesRepository
import com.android.tiltcamera.camera.domain.use_case.CameraUseCases
import com.android.tiltcamera.camera.domain.use_case.GetLastPictureUseCase
import com.android.tiltcamera.camera.domain.use_case.GetPictureCollectionUseCase
import com.android.tiltcamera.camera.domain.use_case.GetActivePictureCollectionsUseCase
import com.android.tiltcamera.camera.domain.use_case.InsertNewCollection
import com.android.tiltcamera.camera.domain.use_case.SavePhotoUseCase
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
object CameraModule {

    @Provides
    @Singleton
    fun provideCameraUseCases(
        @ApplicationContext context: Context,
        preferencesRepository: PreferencesRepository,
        pictureRepository: PictureRepository,
        collectionRepository: CollectionRepository
    ): CameraUseCases {
        return CameraUseCases(
            savePhotoUseCase = SavePhotoUseCase(context, pictureRepository,preferencesRepository),
            getPictureCollectionUseCase = GetPictureCollectionUseCase(collectionRepository),
            getActivePictureCollectionsUseCase = GetActivePictureCollectionsUseCase(collectionRepository),
            getLastPictureUseCase = GetLastPictureUseCase(pictureRepository),
            insertNewCollection = InsertNewCollection(collectionRepository)
        )
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