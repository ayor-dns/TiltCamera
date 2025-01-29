package com.android.tiltcamera.gallery.presentation

import androidx.lifecycle.ViewModel
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val pictureRepository: PictureRepository
) : ViewModel() {


    private val _state = MutableStateFlow<GalleryState>(GalleryState())
    val state = _state.asStateFlow()


    init {

    }
}