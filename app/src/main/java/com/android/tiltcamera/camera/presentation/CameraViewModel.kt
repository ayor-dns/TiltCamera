package com.android.tiltcamera.camera.presentation

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tiltcamera.camera.data.CameraInfoProvider
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.model.CameraResolution
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.PreferencesRepository
import com.android.tiltcamera.camera.domain.sensor.OrientationSensor
import com.android.tiltcamera.camera.domain.use_case.CameraUseCases
import com.android.tiltcamera.core.domain.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val preferenceRepository : PreferencesRepository,
    private val cameraInfoProvider: CameraInfoProvider,
    private val orientationSensor: OrientationSensor,
    private val cameraUseCases: CameraUseCases,
): ViewModel() {

    private val _state = MutableStateFlow(CameraScreenState())
    val state = _state
        .onStart {
            observePicturesCollections()
        }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            _state.value
        )

    private fun observePicturesCollections() {
        viewModelScope.launch {
            cameraUseCases.getPictureCollectionsUseCase().collect { collections ->
                _state.update {
                    it.copy(collections = collections)
                }
            }
        }
    }

    private var azimuth by mutableDoubleStateOf(0.0)
    private var pitch by mutableDoubleStateOf(0.0)
    private var roll by mutableDoubleStateOf(0.0)



    init {
        Timber.d("init")
        initializeOrientationSensor()
        loadPreferences()
        initializeCameraValues()

    }

    private fun initializeOrientationSensor() {
        orientationSensor.startListening()
        orientationSensor.setOnSensorValuesChangedListener { values ->
            azimuth = values[0] * 180 / Math.PI
            pitch = values[1] * 180 / Math.PI
            roll = values[2] * 180 / Math.PI

            _state.update {
                it.copy(
                    azimuth = azimuth,
                    pitch = pitch,
                    roll = roll
                )
            }

            val formattedString = String.format(
                Locale.getDefault(),
                "Azimuth=%.2f°, Pitch=%.2f°, Roll=%.2f°",
                azimuth,
                pitch,
                roll
            )
//            Timber.d(formattedString)
        }
    }

    private fun loadPreferences() {
        val collectionId = preferenceRepository.getCurrentPictureCollectionId()
        collectionId?.let { id ->
            viewModelScope.launch {
                val collection = cameraUseCases.getPictureCollectionUseCase(id)
                _state.update {
                    it.copy(currentCollection = collection)
                }
            }
        }
        val showPictureInfo = preferenceRepository.getShowPictureInfoPreference()
        _state.update {
            it.copy(showPictureInfo = showPictureInfo)
        }
    }

    private fun initializeCameraValues() {
        val availableCamerasResult = cameraInfoProvider.getAvailableCameras()
        Timber.d("availableCamerasResult: $availableCamerasResult")

        when(availableCamerasResult){
            is Result.Error -> {
                Timber.e(availableCamerasResult.error.toString())
            }
            is Result.Success -> {
                val availableCameras = availableCamerasResult.data

                val hasFrontCamera = availableCameras.any { it.lensFacing == CameraSelector.LENS_FACING_FRONT }
                _state.update {
                    it.copy(
                        hasFrontCamera = hasFrontCamera,
                        cameraInfos = availableCameras,
                    )
                }

                updateAvailableResolution(isBackCamera = true)
            }
        }
    }



    fun onAction(action: CameraAction) {
        when(action){
            is CameraAction.OnTakePhoto -> onTakePhoto(action.bitmap)
            is CameraAction.SetAspectRatioMode -> setAspectRatioMode(action.aspectRatioMode)
            is CameraAction.OnCameraResolutionSelected -> setCameraResolution(action.cameraResolution)
            is CameraAction.OnPictureCollectionSelected -> setPicturesCollection(action.picturesCollection)
            is CameraAction.SetShowPictureInfo -> setShowPictureInfo(action.showPictureInfo)
            CameraAction.ShowNewCollectionDialog -> setNewCollectionDialogVisibility(true)
            CameraAction.CancelNewCollectionDialog -> resetNewCollection()
            CameraAction.ConfirmNewCollectionDialog -> TODO()
            is CameraAction.SetNewCollectionAspectRatioMode -> setNewCollectionAspectRatioMode(action.aspectRatioMode)
            is CameraAction.SetNewCollectionName -> setNewCollectionName(action.name)
            CameraAction.DismissNewCollectionDialog -> setNewCollectionDialogVisibility(false)
            is CameraAction.SwitchCamera -> updateCameraSelector(action.cameraSelector)
        }
    }

    private fun setCameraResolution(cameraResolution: CameraResolution) {
        _state.update { it.copy(
            currentResolution = cameraResolution
        ) }
    }

    private fun updateCameraSelector(cameraSelector: CameraSelector) {
        _state.update { it.copy(
            currentCameraSelector = cameraSelector
        ) }
        updateAvailableResolution(cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
    }

    private fun updateAvailableResolution(isBackCamera: Boolean) {
        val lensFacing = if(isBackCamera) CameraSelector.LENS_FACING_BACK else CameraSelector.LENS_FACING_FRONT
        val availableResolutions = getAvailableResolutions(_state.value.cameraInfos.firstOrNull { it.lensFacing == lensFacing }?.supportedResolutions)
        _state.update {it.copy(
            availableResolutions = availableResolutions
        ) }
    }
    private fun getAvailableResolutions(supportedResolutions: List<Size>?): List<CameraResolution> {
        val cameraResolutions = mutableListOf<CameraResolution>()

        val filterAspectRatio = when(_state.value.currentAspectRatioMode){
            AspectRatioMode.RATIO_16_9 -> "16:9"
            AspectRatioMode.RATIO_4_3 ->"4:3"
        }

        supportedResolutions?.forEach { resolution ->
            val aspectRatio = getAspectRatioString(resolution)
            if(filterAspectRatio == aspectRatio){
                val cameraResolution = CameraResolution(
                    cameraResolutionId = 0,
                    displayName = "${resolution.width}x${resolution.height} - $aspectRatio (${getMegaPixels(resolution)})",
                    lensFacing = 0,
                    width = resolution.width,
                    height = resolution.height
                )
                cameraResolutions.add(cameraResolution)
            }
        }
        return cameraResolutions.sortedByDescending { it.width * it.height }
    }

    private fun getAspectRatioString(resolution: Size): String {
        val gcd = gcd(resolution.width, resolution.height)
        val widthRatio = resolution.width / gcd
        val heightRatio = resolution.height / gcd
        return "$widthRatio:$heightRatio"
    }

    private fun gcd(a: Int, b: Int): Int {
        return if (b == 0) a else gcd(b, a % b)
    }

    private fun getMegaPixels(resolution: Size): String {
        val megaPixels = (resolution.width * resolution.height) / 1_000_000.0 // 1 mégapixel = 1 million de pixels
        return String.format("%.1f MP", megaPixels)
    }

    private fun resetNewCollection() {
        _state.update { it.copy(
            newCollection = PicturesCollection.newCollection(),
            nameError = null
        ) }
        setNewCollectionDialogVisibility(false)
    }

    private fun setNewCollectionName(name: String) {
        _state.update { it.copy(
            newCollection = it.newCollection.copy(name = name)
        ) }
    }

    private fun setNewCollectionAspectRatioMode(aspectRatioMode: AspectRatioMode) {
        _state.update { it.copy(
            newCollection = it.newCollection.copy(aspectRatioMode = aspectRatioMode)
        ) }
    }

    private fun setNewCollectionDialogVisibility(isVisible: Boolean) {
        _state.update { it.copy(showNewCollectionDialog = isVisible) }
    }

    private fun setPicturesCollection(picturesCollection: PicturesCollection) {
        _state.update { it.copy(
            currentCollection = picturesCollection
        ) }
        preferenceRepository.setCurrentPictureCollectionId(picturesCollection.collectionId)
    }
    private fun setShowPictureInfo(showPictureInfo: Boolean) {
        _state.update { it.copy(
            showPictureInfo = showPictureInfo
        ) }
        preferenceRepository.setShowPictureInfoPreference(showPictureInfo)
    }
    private fun setAspectRatioMode(aspectRatioMode: AspectRatioMode) {
        // get resolution for aspect ratio

        _state.update { it.copy(
            currentAspectRatioMode = aspectRatioMode
        ) }

        updateAvailableResolution(_state.value.currentCameraSelector == CameraSelector.DEFAULT_BACK_CAMERA)
    }


    private fun onTakePhoto(bitmap: Bitmap) {
        // save to storage and to db
        Timber.d("onTakePhoto")
        _state.update {
            it.copy(isSavingPicture = true)
        }
        viewModelScope.launch {
            val result = cameraUseCases.savePhotoUseCase(bitmap, _state.value.currentCollection, azimuth, pitch, roll)
            Timber.d("result: $result")
        }.invokeOnCompletion {
            _state.update {
                it.copy(isSavingPicture = false)
            }
        }
    }
}