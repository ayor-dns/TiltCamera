package com.android.tiltcamera.gallery.presentation.picture_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tiltcamera.gallery.domain.use_case.PictureUseCases
import kotlinx.coroutines.flow.MutableStateFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PictureDetailViewModel @Inject constructor(
    private val pictureUseCases: PictureUseCases
): ViewModel() {

    private var initializeCalled = false
    private var pictureId: Long? = null

    private val _state = MutableStateFlow(PictureDetailState())
    val state = _state.asStateFlow()

    fun onAction(action: PictureDetailAction){
        when(action){
            PictureDetailAction.OnBackClick -> TODO()
            PictureDetailAction.OnDeleteClick -> TODO()
        }
    }

    fun initialize(pictureId: Long){
        if(initializeCalled) return
        this.pictureId = pictureId
        initializeCalled = true

        loadPictureInfo()
        loadCollectionInfo()
    }



    private fun loadPictureInfo() {
        viewModelScope.launch {
            pictureUseCases.loadPictureInfoUseCase(pictureId).collect { picture ->
                _state.update { it.copy(picture = picture) }
            }
        }
    }
    private fun loadCollectionInfo() {
        viewModelScope.launch {

        }
    }
}