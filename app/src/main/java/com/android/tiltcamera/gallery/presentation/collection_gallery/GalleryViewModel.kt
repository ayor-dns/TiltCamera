package com.android.tiltcamera.gallery.presentation.collection_gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.CollectionRepository
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val collectionRepository: CollectionRepository
) : ViewModel() {


    private val _state = MutableStateFlow(GalleryState())
    val state = _state.asStateFlow()

    private val _picturesCollections = MutableStateFlow<List<PicturesCollection>>(emptyList())
    val picturesCollections = applySearchOnPicturesCollection()

    private fun applySearchOnPicturesCollection(): StateFlow<List<PicturesCollection>> {
        return combine(
            _picturesCollections,
            _state.map { it.searchQuery }
        ) { picturesCollections, searchQuery ->
            val updatedCollections = if(searchQuery.isBlank()) {
                picturesCollections
            } else {
                picturesCollections.filter {
                    it.name.contains(searchQuery, ignoreCase = true)
                }
            }
            updatedCollections
            }
            .onEach { _state.update { it.copy(isLoading = false) } }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    init {
        viewModelScope.launch {
            collectionRepository.getPicturesCollections().collect{
                _picturesCollections.value = it
            }
        }
    }


    fun onAction(action: GalleryAction) {
        when (action) {
            is GalleryAction.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = action.query)
            }
            is GalleryAction.OnCollectionClick -> Unit
        }
    }
}