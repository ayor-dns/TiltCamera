package com.android.tiltcamera.gallery.presentation.collection_detail

import androidx.compose.runtime.Composable
import com.android.tiltcamera.camera.domain.model.Picture

@Composable
fun GalleryDetailScreenRoot(
    viewModel: GalleryDetailViewModel,
    onPictureClick: (picture: Picture) -> Unit,
    onBackClick: () -> Unit
) {

    GalleryDetailScreen(

    )
}

@Composable
private fun GalleryDetailScreen() {
}