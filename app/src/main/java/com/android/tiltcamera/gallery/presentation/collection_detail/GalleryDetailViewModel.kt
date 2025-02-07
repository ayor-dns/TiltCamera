package com.android.tiltcamera.gallery.presentation.collection_detail

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryDetailViewModel @Inject constructor() : ViewModel() {

    private var initializeCalled = false
    private var collectionId: Long? = null

    fun initialize(collectionId: Long){
        if(initializeCalled) return
        this.collectionId = collectionId
        initializeCalled = true
    }

}