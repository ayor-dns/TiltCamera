package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.core.domain.openAppSettings

@Composable
fun NoPermissionScreen(
    modifier: Modifier = Modifier,

) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize()
    ) {
        Column(modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Camera permission declined.\n You can enable it in app settings.",
                textAlign = TextAlign.Center)
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    openAppSettings(context = context)
                }
            ) {
                Text(text = "Open settings")
            }

        }

    }
}