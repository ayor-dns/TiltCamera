package com.android.tiltcamera.camera.presentation

import android.net.Uri
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.android.tiltcamera.camera.data.CameraInfoProvider
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.CameraResolution
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.presentation.components.OptionItem
import com.android.tiltcamera.core.presentation.UiText

data class CameraScreenState(
    val currentCollection: PicturesCollection? = null,
    val collections: List<PicturesCollection> = emptyList(),

    val currentCameraSelector: CameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,

    val availableLensFacing: List<Int> = emptyList(),
    val currentLensFacing: Int = CameraSelector.LENS_FACING_BACK,

    val availableResolutions: Set<CameraResolution> = emptySet(),
    val filteredResolutions: List<CameraResolution> = emptyList(),
    val currentCameraResolution: MutableState<CameraResolution> = mutableStateOf(CameraResolution.default()),

    val currentAspectRatioMode: AspectRatioMode = AspectRatioMode.RATIO_4_3,
    val aspectRatioOptions: Map<Int,List<OptionItem>> = emptyMap(),
    val lensFacingOptions: List<OptionItem> = emptyList(),

    val showPictureInfo: Boolean = true,
    val hasFrontCamera: Boolean = false,
    val cameraInfos: List<CameraInfoProvider.CameraInfo> = emptyList(),

    val isSavingPicture: Boolean = false,
    val lastPictureUri: Uri? = null,

    val azimuth: Double? = null,
    val pitch: Double? = null,
    val roll: Double? = null,

    // NEW COLLECTIONS
    val showNewCollectionDialog: Boolean = false,
    val newCollectionFilteredResolutions: List<CameraResolution> = emptyList(),
    val newCollection: PicturesCollection = PicturesCollection.newCollection(),
    val nameError: UiText? = null,

    )

