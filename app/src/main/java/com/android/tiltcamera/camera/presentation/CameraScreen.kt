package com.android.tiltcamera.camera.presentation

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.tiltcamera.camera.domain.takePhoto
import com.android.tiltcamera.camera.presentation.components.CameraPreview
import com.android.tiltcamera.camera.presentation.components.NoPermissionScreen
import com.android.tiltcamera.core.domain.openAppSettings
import com.android.tiltcamera.core.presentation.CameraPermissionTextProvider
import com.android.tiltcamera.core.presentation.PermissionDialog
import com.android.tiltcamera.core.presentation.Pink
import com.android.tiltcamera.core.presentation.Purple
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

@Composable
fun CameraScreenRoot(
    viewModel: CameraViewModel,

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
            state = viewModel.state.collectAsStateWithLifecycle().value,
            onAction = { action ->

                viewModel.onAction(action)
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraScreen(
    state: CameraScreenState,
    onAction: (CameraAction) -> Unit
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                LifecycleCameraController.IMAGE_CAPTURE
            )
        }
    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 0.dp,
        sheetContent = {

        }
    ) { padding ->
        Box(
            modifier = Modifier.border(4.dp, brush = Brush.linearGradient(
                colors = listOf(Pink, Purple),
                start = Offset(0f, Float.POSITIVE_INFINITY),
                end = Offset(Float.POSITIVE_INFINITY, 0f)
            ),
                RoundedCornerShape(36.dp)
            )
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(
                controller = controller,
                modifier = Modifier
                    .fillMaxSize()
            )

            IconButton(
                onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                },
                modifier = Modifier
                    .offset(16.dp, 32.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.5f))
            ) {
                Icon(
                    imageVector = Icons.Default.Cameraswitch,
                    contentDescription = "Switch camera"
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset((-4).dp, (40).dp)
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


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .offset(0.dp, (-40).dp)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f)),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Open Bottom sheet"
                    )
                }
                IconButton(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.5f)),
                    onClick = {
                        Timber.d("take photo")
                        takePhoto(
                            context = context,
                            controller = controller,
                            onPhotoTaken = { bitmap ->
                                onAction(CameraAction.OnTakePhoto(bitmap))
                            }
                        )
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Take photo"
                    )
                }
            }
        }
    }
}