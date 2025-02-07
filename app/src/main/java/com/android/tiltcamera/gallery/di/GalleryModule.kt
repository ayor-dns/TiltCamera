package com.android.tiltcamera.gallery.di

import com.android.tiltcamera.core.domain.repository.PictureRepository
import com.android.tiltcamera.gallery.domain.use_case.LoadPictureInfoUseCase
import com.android.tiltcamera.gallery.domain.use_case.PictureUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object GalleryModule {

    @Provides
    @Singleton
    fun providePictureUseCases(
        pictureRepository: PictureRepository,
    ): PictureUseCases {
        return PictureUseCases(
            loadPictureInfoUseCase = LoadPictureInfoUseCase(pictureRepository)
        )
    }
}