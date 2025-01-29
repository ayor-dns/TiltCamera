package com.android.tiltcamera.gallery.presentation.collection_gallery.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.camera.domain.model.PicturesCollection

@Composable
fun CollectionList(
    collections: List<PicturesCollection>,
    onCollectionClick: (PicturesCollection) -> Unit,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState()
) {
    val dropDownItems = listOf(
        DropDownCollectionListItem(text = "Supprimer",eventType = EventType.DELETE),
        DropDownCollectionListItem(text = "Masquer",eventType = EventType.HIDE)
    )


    LazyColumn(
        modifier = modifier,
        state = scrollState,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(items = collections, key = { it.collectionId }
        ) { picturesCollection ->
            CollectionListItem(
                modifier = Modifier.padding(horizontal = 16.dp),
                picturesCollection = picturesCollection,
                imageUri = null,
                onCollectionClick = {
                    onCollectionClick(picturesCollection)
                },
                onDropDownItemClick = {item ->

                },
                dropDownItems = dropDownItems
            )
        }
    }
}