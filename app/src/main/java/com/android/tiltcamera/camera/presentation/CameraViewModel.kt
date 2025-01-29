package com.android.tiltcamera.camera.presentation

import android.graphics.Bitmap
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.data.CameraInfoProvider
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.CameraResolution
import com.android.tiltcamera.camera.domain.PicturesCollectionValidator
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.PreferencesRepository
import com.android.tiltcamera.camera.domain.sensor.OrientationSensor
import com.android.tiltcamera.camera.domain.use_case.CameraUseCases
import com.android.tiltcamera.camera.presentation.components.OptionItem
import com.android.tiltcamera.core.domain.Result
import com.android.tiltcamera.core.domain.RoomError
import com.android.tiltcamera.core.presentation.asUiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.math.abs

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val preferenceRepository : PreferencesRepository,
    private val cameraInfoProvider: CameraInfoProvider,
    private val orientationSensor: OrientationSensor,
    private val cameraUseCases: CameraUseCases,
    private val picturesCollectionValidator : PicturesCollectionValidator
): ViewModel() {

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

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

    private var azimuth by mutableStateOf<Double?>(null)
    private var pitch by mutableStateOf<Double?>(null)
    private var roll by mutableStateOf<Double?>(null)



    init {
        Timber.d("init CameraViewModel")
        initializeOrientationSensor()
        initializeCameraValues()
        loadPreferences()
        updateFilteredResolution()
        updateNewCollectionFilteredResolution()
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

//            Timber.d(String.format(Locale.getDefault(),"Azimuth=%.2f°, Pitch=%.2f°, Roll=%.2f°",azimuth,pitch,roll))
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

                val availableResolutions = mutableSetOf<CameraResolution>()
                availableCameras.forEach { cameraInfo ->
                    val cameraResolutions = supportedResolutionsToCameraResolution(cameraInfo.supportedResolutions, cameraInfo.lensFacing)
                    availableResolutions.addAll(cameraResolutions)
                }

                val aspectRatioOptions = mutableMapOf<Int, MutableList<OptionItem>>()
                availableResolutions.groupBy { it.lensFacing }.forEach { (lensFacing, resolutions) ->
                    if(resolutions.any { it.aspectRatioMode == AspectRatioMode.RATIO_16_9 }) {
                        if(aspectRatioOptions[lensFacing] == null) aspectRatioOptions[lensFacing] = mutableListOf()
                        aspectRatioOptions[lensFacing]!!.add(OptionItem(id = 0, icon = R.drawable.format_16_9, data = AspectRatioMode.RATIO_16_9))
                    }
                    if(resolutions.any { it.aspectRatioMode == AspectRatioMode.RATIO_4_3 }) {
                        if(aspectRatioOptions[lensFacing] == null) aspectRatioOptions[lensFacing] = mutableListOf()
                        aspectRatioOptions[lensFacing]!!.add(OptionItem(id = 1, icon = R.drawable.format_4_3, data = AspectRatioMode.RATIO_4_3))
                    }
                }

                val availableLensFacing = availableCameras.map { it.lensFacing }.distinct()
                val lensFacingOptions = mutableListOf<OptionItem>()
                if(availableLensFacing.contains(CameraSelector.LENS_FACING_FRONT)){
                    lensFacingOptions.add(OptionItem(id = 0, icon = R.drawable.camera_front_fill1_wght300, data = CameraSelector.LENS_FACING_FRONT))
                }
                if(availableLensFacing.contains(CameraSelector.LENS_FACING_BACK)){
                    lensFacingOptions.add(OptionItem(id = 1, icon = R.drawable.camera_rear_fill1_wght300, data = CameraSelector.LENS_FACING_BACK))
                }

                _state.update {
                    it.copy(
                        hasFrontCamera = hasFrontCamera,
                        cameraInfos = availableCameras,
                        availableResolutions = availableResolutions,
                        aspectRatioOptions = aspectRatioOptions,
                        availableLensFacing = availableLensFacing,
                        lensFacingOptions = lensFacingOptions
                    )
                }
            }
        }
    }
    private fun supportedResolutionsToCameraResolution(supportedResolutions: List<Size>?, lensFacing: Int): List<CameraResolution> {
        val cameraResolutions = mutableListOf<CameraResolution>()

        supportedResolutions?.forEach { resolution ->
            val cameraResolution = CameraResolution.fromSizeAndLensFacing(resolution, lensFacing)
            cameraResolutions.add(cameraResolution)
        }
        return cameraResolutions.sortedByDescending { it.resolution.width * it.resolution.height }
    }
    private fun loadPreferences() {
        val showPictureInfo = preferenceRepository.getShowPictureInfoPreference()
        _state.update {
            it.copy(showPictureInfo = showPictureInfo)
        }

        val collectionId = preferenceRepository.getCurrentPictureCollectionId()
        Timber.d("loadPreferences: collectionId: $collectionId")
        updatePicturesCollectionRelatedValues(collectionId)

    }

    private fun updatePicturesCollectionRelatedValues(collectionId: Long?) {
        if(collectionId != null){
            viewModelScope.launch {
                val collection = cameraUseCases.getPictureCollectionUseCase(collectionId)
                val aspectModeRatio = collection.aspectRatioMode
                val lastPictureUri = cameraUseCases.getLastPictureUriUseCase(collectionId)
                val lensFacing = collection.lensFacing
                val cameraSelector = when(lensFacing){
                    CameraSelector.LENS_FACING_BACK -> CameraSelector.DEFAULT_BACK_CAMERA
                    CameraSelector.LENS_FACING_FRONT -> CameraSelector.DEFAULT_FRONT_CAMERA
                    else -> CameraSelector.DEFAULT_BACK_CAMERA
                }
                val cameraResolution = _state.value.availableResolutions.firstOrNull {
                    it.resolution == collection.cameraResolution &&
                        it.lensFacing == lensFacing &&
                        it.aspectRatioMode == aspectModeRatio
                } ?: CameraResolution.default()

                Timber.d("cameraResolution=$cameraResolution")

                _state.update {
                    it.copy(
                        currentCollection = collection,
                        currentCameraResolution = mutableStateOf(cameraResolution),
                        currentLensFacing = lensFacing,
                        currentAspectRatioMode = aspectModeRatio,
                        lastPictureUri = lastPictureUri,
                        currentCameraSelector = cameraSelector
                    )
                }
            }.invokeOnCompletion { updateFilteredResolution() }
        } else {
            // set default value
            _state.update {
                it.copy(
                    currentAspectRatioMode = AspectRatioMode.RATIO_4_3,
                    currentCollection = null,
                    currentCameraResolution = mutableStateOf(getMaxResolutionAvailable()),
                    lastPictureUri = null,
                    currentCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA,
                    currentLensFacing = CameraSelector.LENS_FACING_BACK
                )
            }
            updateFilteredResolution()
        }
    }
    private fun getMaxResolutionAvailable(): CameraResolution {
        val filteredResolutions = _state.value.availableResolutions.filter { cameraResolution ->
            cameraResolution.aspectRatioMode == _state.value.currentAspectRatioMode &&
                    cameraResolution.lensFacing == _state.value.currentLensFacing
        }
        return filteredResolutions.maxByOrNull { it.resolution.width * it.resolution.height } ?: CameraResolution.default(_state.value.currentAspectRatioMode, _state.value.currentLensFacing)
    }


    fun onAction(action: CameraAction) {
        when(action){
            // take photo
            is CameraAction.OnTakePhoto -> onTakePhoto(action.bitmap)


            // configure camera
            is CameraAction.OnPictureCollectionSelected -> setPicturesCollection(action.picturesCollection)
            is CameraAction.OnCameraSelectorChanged -> setCameraSelector(action.cameraSelector, action.lensFacing)
            is CameraAction.OnAspectRatioModeSelected -> setAspectRatioMode(action.aspectRatioMode)
            is CameraAction.OnCameraResolutionSelected -> setCameraResolution(action.cameraResolution)
            is CameraAction.SetShowPictureInfo -> setShowPictureInfo(action.showPictureInfo)


            // create new collection
            CameraAction.ShowNewCollectionDialog -> setNewCollectionDialogVisibility(true)
            is CameraAction.SetNewCollectionName -> setNewCollectionName(action.name)
            is CameraAction.SetNewCollectionAspectRatioMode -> setNewCollectionAspectRatioMode(action.aspectRatioMode)
            is CameraAction.SetNewCollectionCameraResolution -> setNewCollectionCameraResolution(action.cameraResolution)
            is CameraAction.SetNewCollectionLensFacing -> setNewCollectionLensFacing(action.lensFacing)
            CameraAction.DismissNewCollectionDialog -> setNewCollectionDialogVisibility(false)
            CameraAction.CancelNewCollectionDialog -> resetNewCollection()
            CameraAction.ConfirmNewCollectionDialog -> confirmNewCollection()
        }
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


    private fun updateFilteredResolution() {
        val filteredResolutions = _state.value.availableResolutions.filter { cameraResolution ->
            cameraResolution.aspectRatioMode == _state.value.currentAspectRatioMode &&
                    cameraResolution.lensFacing == _state.value.currentLensFacing
        }

        val newCameraResolution: MutableState<CameraResolution>
        val closestResolution =  getClosestResolution(_state.value.currentCameraResolution.value.resolution, filteredResolutions)
        val resolution = filteredResolutions.firstOrNull { it.resolution == closestResolution }
        newCameraResolution = if(resolution != null){
            mutableStateOf(resolution)
        } else {
            mutableStateOf(CameraResolution.default(_state.value.currentAspectRatioMode, _state.value.currentLensFacing))
        }
        Timber.d("newCameraResolution=$newCameraResolution")

        _state.update {it.copy(
            filteredResolutions = filteredResolutions,
            currentCameraResolution = newCameraResolution
        ) }
    }
    private fun updateNewCollectionFilteredResolution() {
        val filteredResolutions = _state.value.availableResolutions.filter { cameraResolution ->
            cameraResolution.aspectRatioMode == _state.value.newCollection.aspectRatioMode &&
                    cameraResolution.lensFacing == _state.value.newCollection.lensFacing
        }

        val newCollectionCameraResolution = if(_state.value.newCollection.cameraResolution in filteredResolutions.map { it.resolution }){
            _state.value.newCollection.cameraResolution
        } else {
            getClosestResolution(_state.value.newCollection.cameraResolution, filteredResolutions)
        }

        _state.update {it.copy(
            newCollectionFilteredResolutions = filteredResolutions,
            newCollection = it.newCollection.copy(cameraResolution = newCollectionCameraResolution)
        ) }
    }
    private fun getClosestResolution(cameraResolution: Size, filteredResolutions: List<CameraResolution>): Size {
        var bestResolution = filteredResolutions.firstOrNull()?.resolution
        var bestScore = Int.MAX_VALUE

        for (resolution in filteredResolutions) {
            val score = calculateScore(cameraResolution, resolution.resolution)

            if (score < bestScore) {
                bestScore = score
                bestResolution = resolution.resolution
            }
        }

        return bestResolution ?: Size(Int.MAX_VALUE, Int.MAX_VALUE)

    }
    private fun calculateScore(targetResolution: Size, candidateResolution: Size): Int {
        val targetWidth = targetResolution.width
        val targetHeight = targetResolution.height

        val candidateWidth = candidateResolution.width
        val candidateHeight = candidateResolution.height

        val areaDiff = abs(targetWidth * targetHeight - candidateWidth * candidateHeight)

        return areaDiff
    }


    private fun setPicturesCollection(picturesCollection: PicturesCollection) {
        _state.update { it.copy(
            currentCollection = picturesCollection
        ) }
        preferenceRepository.setCurrentPictureCollectionId(picturesCollection.collectionId)
        updatePicturesCollectionRelatedValues(picturesCollection.collectionId)
    }
    private fun setCameraSelector(cameraSelector: CameraSelector, lensFacing: Int) {
        _state.update { it.copy(
            currentCameraSelector = cameraSelector,
            currentLensFacing = lensFacing
        ) }
        updateFilteredResolution()
    }
    private fun setAspectRatioMode(aspectRatioMode: AspectRatioMode) {
        _state.update { it.copy(
            currentAspectRatioMode = aspectRatioMode
        ) }

        updateFilteredResolution()
    }
    private fun setCameraResolution(cameraResolution: CameraResolution) {
        _state.update { it.copy(
            currentCameraResolution = mutableStateOf(cameraResolution)
        ) }
    }
    private fun setShowPictureInfo(showPictureInfo: Boolean) {
        _state.update { it.copy(
            showPictureInfo = showPictureInfo
        ) }
        preferenceRepository.setShowPictureInfoPreference(showPictureInfo)
    }


    private fun setNewCollectionDialogVisibility(isVisible: Boolean) {
        _state.update { it.copy(showNewCollectionDialog = isVisible) }
    }
    private fun setNewCollectionName(name: String) {
        val cleanedName = cleanName(name)

        _state.update { it.copy(
            newCollection = it.newCollection.copy(name = cleanedName)
        ) }
    }
    private fun cleanName(name: String): String {
        val regex = Regex("[^a-zA-Z0-9_\\-/ ]")
        return regex.replace(name, "")
    }
    private fun setNewCollectionAspectRatioMode(aspectRatioMode: AspectRatioMode) {
        _state.update { it.copy(
            newCollection = it.newCollection.copy(aspectRatioMode = aspectRatioMode)
        ) }
        updateNewCollectionFilteredResolution()

    }
    private fun setNewCollectionLensFacing(lensFacing: Int) {
        _state.update { it.copy(
            newCollection = it.newCollection.copy(lensFacing = lensFacing)
        ) }
        updateNewCollectionFilteredResolution()
    }
    private fun setNewCollectionCameraResolution(cameraResolution: CameraResolution) {
        _state.update { it.copy(
            newCollection = it.newCollection.copy(cameraResolution = cameraResolution.resolution)
        ) }
    }
    private fun resetNewCollection() {
        _state.update { it.copy(
            newCollection = PicturesCollection.newCollection(),
            nameError = null
        ) }
        updateNewCollectionFilteredResolution()
        setNewCollectionDialogVisibility(false)

    }
    private fun confirmNewCollection() {

        viewModelScope.launch {
            // validate name
            val nameResult = picturesCollectionValidator.validateName(_state.value.newCollection.name)

            _state.update { it.copy(
                nameError = when(nameResult){
                    is Result.Success -> null
                    is Result.Error -> nameResult.error.asUiText()
                }
            ) }

            val hasError = listOf(nameResult).any { it is Result.Error}
            if(hasError) return@launch

            // insert new collection
            val result = cameraUseCases.insertNewCollection(_state.value.newCollection.copy(name = _state.value.newCollection.name.trim()))
            if(result is Result.Success) {
                resetNewCollection()
                updatePicturesCollectionRelatedValues(result.data)
            }
            validationEventChannel.send(ValidationEvent.InsertNewCollectionResult(result))
        }
    }

    sealed class ValidationEvent {
        data class InsertNewCollectionResult(val result: Result<Long, RoomError>): ValidationEvent()
    }

}