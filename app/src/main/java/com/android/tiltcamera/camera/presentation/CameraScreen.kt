package com.android.tiltcamera.camera.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.util.Size
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.resolutionselector.AspectRatioStrategy
import androidx.camera.core.resolutionselector.ResolutionSelector
import androidx.camera.core.resolutionselector.ResolutionStrategy
import androidx.camera.core.resolutionselector.ResolutionStrategy.FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.tiltcamera.R
import com.android.tiltcamera.app.Route
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.takePhoto
import com.android.tiltcamera.camera.presentation.components.CameraOptionBottomSheet
import com.android.tiltcamera.camera.presentation.components.CameraPreview
import com.android.tiltcamera.camera.presentation.components.LastPicturePreview
import com.android.tiltcamera.camera.presentation.components.NewCollectionDialog
import com.android.tiltcamera.camera.presentation.components.NoPermissionScreen
import com.android.tiltcamera.core.domain.Result
import com.android.tiltcamera.core.presentation.Pink
import com.android.tiltcamera.core.presentation.Purple
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import java.util.Locale

@Composable
fun CameraScreenRoot(
    viewModel: CameraViewModel,
    onNavigate: (route: Route) -> Unit,
) {
    val context = LocalContext.current
    val activity = LocalActivity.current
    val state = viewModel.state.collectAsStateWithLifecycle().value

    var permissionState by rememberSaveable { mutableStateOf(ContextCompat.checkSelfPermission(
        context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        permissionState = isGranted
    }

    if (!permissionState) {
        if (shouldShowRequestPermissionRationale(activity!!, Manifest.permission.CAMERA)) {
            // Expliquez pourquoi vous avez besoin de la permission et redemandez-la
            Button(onClick = { launcher.launch(Manifest.permission.CAMERA) }) {
                Text("Demander la permission")
            }
        } else {

            NoPermissionScreen()
        }
    } else {
        CameraScreen(
            state = state,
            onAction = { action ->

                viewModel.onAction(action)
            },
            validationEvents = viewModel.validationEvents,
            onNavigate = onNavigate
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    state: CameraScreenState,
    validationEvents: Flow<CameraViewModel.ValidationEvent>,
    onAction: (CameraAction) -> Unit,
    onNavigate: (route: Route) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val sheetState = rememberModalBottomSheetState()
    var isSheetOpen by rememberSaveable {
        mutableStateOf(false)
    }

    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE
            )
            cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        }
    }

    LaunchedEffect(key1 = state.currentAspectRatioMode){
        val aspectRatioStrategy = when(state.currentAspectRatioMode){
            AspectRatioMode.RATIO_16_9 -> AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY
            AspectRatioMode.RATIO_4_3 -> AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY
            AspectRatioMode.RATIO_OTHER -> AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY
        }
        controller.previewResolutionSelector = ResolutionSelector.Builder().setAspectRatioStrategy(aspectRatioStrategy).build()
        controller.imageCaptureResolutionSelector = ResolutionSelector.Builder().setAspectRatioStrategy(aspectRatioStrategy).build()
    }

    LaunchedEffect(key1 = state.currentCameraSelector) {
        controller.cameraSelector = state.currentCameraSelector
    }

    LaunchedEffect(key1 = state.currentCameraResolution) {
        val targetWidth = state.currentCameraResolution.value.resolution.width
        val targetHeight = state.currentCameraResolution.value.resolution.height

        val aspectRatioStrategy = when(state.currentAspectRatioMode){
            AspectRatioMode.RATIO_16_9 -> AspectRatioStrategy.RATIO_16_9_FALLBACK_AUTO_STRATEGY
            AspectRatioMode.RATIO_4_3 -> AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY
            AspectRatioMode.RATIO_OTHER -> AspectRatioStrategy.RATIO_4_3_FALLBACK_AUTO_STRATEGY
        }

        controller.imageCaptureResolutionSelector = ResolutionSelector.Builder()
            .setAspectRatioStrategy(aspectRatioStrategy)
            .setResolutionStrategy(ResolutionStrategy(Size(targetWidth, targetHeight),FALLBACK_RULE_CLOSEST_HIGHER_THEN_LOWER)).build()


    }

    LaunchedEffect(key1 = Unit) {
        validationEvents.collect { event ->
            if(event is CameraViewModel.ValidationEvent.InsertNewCollectionResult){
                if(event.result is Result.Success){
                    isSheetOpen = false
                }
            }
            handleValidationEvents(event, snackbarHostState)
        }
    }


    if(isSheetOpen){
        CameraOptionBottomSheet(
            onDismissRequest = { isSheetOpen = false },
            sheetState = sheetState,
            onAction = onAction,
            state = state
        )
    }

    NewCollectionDialog(
        showDialog = state.showNewCollectionDialog,
        picturesCollection = state.newCollection,
        filteredResolutions = state.newCollectionFilteredResolutions,
        cameraOptions = state.lensFacingOptions,
        aspectRatioOptions = state.aspectRatioOptions[state.newCollection.lensFacing] ?: emptyList(),
        nameError = state.nameError,
        onAction = onAction
    )

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Brush.linearGradient(
            colors = listOf(Pink, Purple),
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        ))){
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            containerColor = Color.Transparent
        ) { innerPadding ->
            Box(modifier = Modifier.padding(top = 16.dp)
                .padding(innerPadding)
                .fillMaxSize()) {

                CameraPreview(
                    controller = controller,
                    modifier = Modifier.fillMaxSize()
                )


                // azimuth, pitch, roll info
                if(state.showPictureInfo) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset((0).dp, (16).dp)
                            .widthIn(min = 145.dp)
                            .background(Color.White.copy(alpha = 0.4f)),
                        horizontalAlignment = Alignment.End,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "Azimuth=%.2f°",
                                state.azimuth
                            )
                        )
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "Pitch=%.2f°",
                                state.pitch
                            )
                        )
                        Text(
                            text = String.format(
                                Locale.getDefault(),
                                "Roll=%.2f°",
                                state.roll
                            )
                        )
                    }
                }

                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.weight(3.8f))
                    Row(modifier = Modifier.weight(1f)){
                        // take picture button
                        IconButton(
                            modifier = Modifier
                                .clip(CircleShape)
                                .size(80.dp)
                                .background(Color.White.copy(alpha = 0.5f)),
                            onClick = {
                                Timber.d("take photo. isSavingPicture=${state.isSavingPicture}")
                                if(!state.isSavingPicture){
                                    takePhoto(
                                        context = context,
                                        controller = controller,
                                        onPhotoTaken = { bitmap ->
                                            onAction(CameraAction.OnTakePhoto(bitmap))
                                        }
                                    )
                                }
                            }
                        ) {
                            if(state.isSavingPicture){
                                CircularProgressIndicator()
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = "Take photo"
                                )
                            }
                        }
                    }
                }


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // OPTIONS
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f)),
                        onClick = {
                            isSheetOpen = true
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.settings_photo_camera_24dp_fill1_wght300),
                            contentDescription = "Open Bottom sheet"
                        )
                    }

                    // last picture
                    LastPicturePreview(
                        lastPictureUri = state.lastPictureUri,
                        modifier = Modifier
                            .size(48.dp)
                    )


                    // switch camera
                    IconButton(
                        enabled = state.hasFrontCamera,
                        onClick = {
                            if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                                onAction(CameraAction.OnCameraSelectorChanged(CameraSelector.DEFAULT_FRONT_CAMERA, CameraSelector.LENS_FACING_FRONT))
                            } else {
                                onAction(CameraAction.OnCameraSelectorChanged(CameraSelector.DEFAULT_BACK_CAMERA, CameraSelector.LENS_FACING_BACK))
                            }
                        },
                        modifier = Modifier
                            .size(48.dp)
                            .alpha(if(state.hasFrontCamera) 1f else 0f)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Cameraswitch,
                            contentDescription = "Switch camera"
                        )
                    }


                    // GALLERY
                    IconButton(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.5f)),
                        onClick = {
                            onNavigate(Route.GalleryScreen)
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.photo_library_24dp_fill1_wght300),
                            contentDescription = "Open gallery"
                        )
                    }

                }

            }
        }
    }
}


suspend fun handleValidationEvents(
    event: CameraViewModel.ValidationEvent,
    snackbarHostState: SnackbarHostState
) {
    var message = ""

    when(event){
        is CameraViewModel.ValidationEvent.InsertNewCollectionResult -> {
            message = when(event.result) {
                is Result.Error -> "Échec de l'ajout de la nouvelle collection."
                is Result.Success -> "Nouvelle collection ajoutée"
            }
        }
    }

    showSnackBarMessage(snackbarHostState, message)
}

suspend fun showSnackBarMessage(
    snackbarHostState: SnackbarHostState,
    message: String,
    duration: SnackbarDuration = SnackbarDuration.Short,
    withDismissAction: Boolean = true
){
    snackbarHostState.showSnackbar(message = message, duration = duration, withDismissAction = withDismissAction)
}