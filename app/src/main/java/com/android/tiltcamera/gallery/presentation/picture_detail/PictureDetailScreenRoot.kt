package com.android.tiltcamera.gallery.presentation.picture_detail

import android.content.Intent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImagePainter
import coil3.compose.rememberAsyncImagePainter
import com.android.tiltcamera.R
import com.android.tiltcamera.core.presentation.BlackGradientOverlay
import com.android.tiltcamera.core.presentation.Pink
import com.android.tiltcamera.core.presentation.Purple
import com.android.tiltcamera.core.presentation.components.BasicTextWithLabel
import com.android.tiltcamera.core.presentation.components.IconWithText
import com.android.tiltcamera.core.presentation.formatMillisToDateTime
import com.android.tiltcamera.core.presentation.notEditableText
import com.android.tiltcamera.gallery.presentation.components.BackTopBar
import com.android.tiltcamera.gallery.presentation.picture_detail.components.ImageDetailBottomSheet
import kotlinx.coroutines.launch

@Composable
fun PictureDetailScreenRoot(
    viewModel: PictureDetailViewModel,
    onBackClick: () -> Unit
) {
    PictureDetailScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        onAction = { action ->
            when(action) {
                PictureDetailAction.OnBackClick -> onBackClick()
                PictureDetailAction.OnDeleteClick -> TODO()
            }
        }
    )

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PictureDetailScreen(
    state: PictureDetailState,
    onAction: (PictureDetailAction) -> Unit,
){
    val context = LocalContext.current
    var imageLoadResult by remember {
        mutableStateOf<Result<Painter>?>(null)
    }
    val painter = rememberAsyncImagePainter(
        model = state.picture?.pictureUri,
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

    val scaffoldState = rememberBottomSheetScaffoldState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.linearGradient(
            colors = listOf(Pink, Purple),
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        ))) {

        BottomSheetScaffold(
            modifier = Modifier.windowInsetsPadding(WindowInsets.safeContent) ,
            containerColor = Color.Transparent,
            sheetContent = {
                state.picture?.let{
                    ImageDetailBottomSheet(
                        picture = it
                    )
                }
            },
            scaffoldState = scaffoldState,
            sheetPeekHeight = 0.dp,

        ){ innerPadding ->

            Box(modifier = Modifier.padding(top = 16.dp)
                .padding(innerPadding)
                .fillMaxSize())
            {
                                // IMAGE
                when (val result = imageLoadResult) {
                    null -> Unit
                    else -> {
                        Image(
                            modifier = Modifier.align(Alignment.Center),
                            painter = if (result.isSuccess) painter else {
                                painterResource(R.drawable.exclamation_24dp_fill1_wght300)
                            },
                            contentDescription = "picture",
                            contentScale = ContentScale.Crop
                        )

                    }
                }

                BackTopBar(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .align(Alignment.TopStart)
                        .clip(CircleShape)
                        .background(Pink),
                    title = state.collection?.collectionName,
                    onBackClick = {
                        onAction(PictureDetailAction.OnBackClick)
                    }
                )

                IconButton(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .align(Alignment.TopEnd)
                        .clip(CircleShape)
                        .background(Pink),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.expand()
                        }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.more_vert_fill1_wght300),
                        contentDescription = "back",
                        tint = Color.White
                    )
                }

                // ACTION BOTTOM BAR SHARE & DELETE
                state.picture?.let{
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Spacer(modifier = Modifier.weight(1f))
                        IconWithText(
                            modifier = Modifier.weight(1f),
                            icon = R.drawable.share_fill0_wght300_,
                            text = "Partager",
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply{
                                    type = "image/jpeg"
                                    putExtra(Intent.EXTRA_STREAM, it.pictureUri)
                                    addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Partager l'image"))
                            }
                        )
                        Spacer(modifier = Modifier.width(40.dp))

                        IconWithText(
                            modifier = Modifier.weight(1f),
                            icon = R.drawable.delete_fill0_wght300,
                            text = "Supprimer",
                            onClick = {

                            }
                        )
                        Spacer(modifier = Modifier.weight(1f))
                    }

                }

            }
        }
    }
}