package com.android.tiltcamera.camera.presentation.components

import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner

@Composable
fun CameraPreview(
    controller: LifecycleCameraController,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    AndroidView(
        factory = {
            PreviewView(it).apply {
                this.scaleType = PreviewView.ScaleType.FIT_START
                this.controller = controller
                controller.bindToLifecycle(lifecycleOwner)
                this.setBackgroundColor(Color.Black.copy(alpha = 0f).toArgb())
            }
        },
        modifier = modifier
    )
}