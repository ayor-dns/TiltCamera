package com.android.tiltcamera.gallery.presentation.collection_gallery

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.core.presentation.Pink
import com.android.tiltcamera.core.presentation.Purple
import com.android.tiltcamera.gallery.presentation.collection_gallery.components.CollectionList
import com.android.tiltcamera.gallery.presentation.collection_gallery.components.SearchBar

@Composable
fun GalleryScreenRoot(
    viewModel: GalleryViewModel,
    onCollectionClick: (PicturesCollection) -> Unit
) {
    GalleryScreen(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        picturesCollections = viewModel.picturesCollections.collectAsStateWithLifecycle().value,
        onAction = {action ->
            when(action){
                is GalleryAction.OnCollectionClick -> onCollectionClick(action.collection)
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}


@Composable
private fun GalleryScreen(
    state: GalleryState,
    picturesCollections: List<PicturesCollection>,
    onAction: (GalleryAction) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val snackbarHostState = remember { SnackbarHostState() }
    val searchResultsListState = rememberLazyListState()

    LaunchedEffect(picturesCollections) {
        searchResultsListState.animateScrollToItem(0)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(
            Brush.linearGradient(
            colors = listOf(Pink, Purple),
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        ))){
        Scaffold(
            containerColor = Color.Transparent,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                // back button
            }

        ) { innerPadding ->

            Column(modifier = Modifier.padding(innerPadding)) {

                Spacer(modifier = Modifier.height(8.dp))

                SearchBar(
                    searchQuery = state.searchQuery,
                    onSearchQueryChange = {
                        onAction(GalleryAction.OnSearchQueryChange(it))
                    },
                    onImeSearch = {
                        keyboardController?.hide()
                    },
                    modifier = Modifier
                        .widthIn(max = 400.dp)
                        .fillMaxWidth()
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp, 16.dp, 0.dp, 0.dp))
                        .background(Color.White),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if(state.isLoading) {
                        CircularProgressIndicator()
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                state.errorMessage != null -> {
                                    Text(
                                        text = state.errorMessage.asString(),
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                picturesCollections.isEmpty() && state.searchQuery.isNotBlank() -> {
                                    Text(
                                        text = "Aucun résultat",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                picturesCollections.isEmpty() -> {
                                    Text(
                                        text = "Pas de collection",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.headlineSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                                else -> {
                                    CollectionList(
                                        collections = picturesCollections,
                                        onCollectionClick = {
                                            onAction(GalleryAction.OnCollectionClick(it))
                                        },
                                        modifier = Modifier.fillMaxSize(),
                                        scrollState = searchResultsListState
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

