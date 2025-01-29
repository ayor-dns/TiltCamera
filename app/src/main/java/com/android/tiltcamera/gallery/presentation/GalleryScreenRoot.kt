package com.android.tiltcamera.gallery.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.android.tiltcamera.camera.domain.model.Picture

@Composable
fun GalleryScreenRoot(
    viewModel: GalleryViewModel
) {

    GalleryScreen(

    )
}


@Composable
fun GalleryScreen(
) {
    Scaffold { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

        }
    }


}

