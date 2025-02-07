package com.android.tiltcamera.camera.presentation.components

import android.net.Uri
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.android.tiltcamera.R

@Composable
fun LastPicturePreview(
    modifier: Modifier = Modifier,
    lastPictureUri: Uri?,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(CircleShape)
//            .background(Color.White.copy(alpha = 0.5f))
            ,
        contentAlignment = Alignment.Center
    ) {
        var imageLoadResult by remember {
            mutableStateOf<Result<Painter>?>(null)
        }
        val painter = rememberAsyncImagePainter(
            model = lastPictureUri,
            onSuccess = {
                imageLoadResult =
                    if (it.painter.intrinsicSize.width > 1 && it.painter.intrinsicSize.height > 1) {
                        Result.success(it.painter)
                    } else {
                        Result.failure(Exception("Invalid image size"))
                    }
            },
            onError = {
                it.result.throwable.printStackTrace()
                imageLoadResult = Result.failure(it.result.throwable)
            }
        )

        val painterState by painter.state.collectAsStateWithLifecycle()
        val transition by animateFloatAsState(
            targetValue = if(painterState is AsyncImagePainter.State.Success) {
                1f
            } else {
                0f
            },
            animationSpec = tween(durationMillis = 800)
        )

        when (val result = imageLoadResult) {
            null -> Unit
            else -> {
                Image(
                    painter = if (result.isSuccess) painter else {
                        painterResource(R.drawable.exclamation_24dp_fill1_wght300)
                    },
                    contentDescription = "last picture",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.alpha(transition)
                )
            }
        }
    }
}