package com.android.tiltcamera.gallery.presentation.collection_gallery.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.core.domain.getTextColor
import com.android.tiltcamera.core.presentation.Blue
import com.android.tiltcamera.core.presentation.SentinelPro

@Composable
fun CollectionListItem(
    modifier: Modifier = Modifier,
    picturesCollection: PicturesCollection,
    imageUri: Uri? = null,
    onCollectionClick: () -> Unit,
    onDropDownItemClick: (item: DropDownCollectionListItem) -> Unit,
    dropDownItems: List<DropDownCollectionListItem> = emptyList(),
) {

    var isContextMenuVisible by rememberSaveable {
        mutableStateOf(false)
    }


    ElevatedCard(
        modifier = modifier
            .fillMaxWidth(0.90f)
            .aspectRatio(16 / 11f)
            .padding(16.dp)
            .clickable(onClick = onCollectionClick ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 8.dp
        ),
    ) {
        Box(modifier = Modifier.fillMaxSize()){
            if(imageUri != null){
                AsyncImage(
                    modifier = Modifier.fillMaxSize(),
                    model = imageUri,
                    contentDescription = "Collection image",
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.new_category_placeholder),
                    contentDescription = "Placeholder image",
                    contentScale = ContentScale.Crop
                )
            }

            Row(modifier = Modifier.align(Alignment.TopEnd)) {

                IconButton(modifier = Modifier
                    .clip(CircleShape),
                    onClick = { isContextMenuVisible = !isContextMenuVisible }) {
                    Icon(
                        modifier = Modifier,
                        painter = painterResource(id = R.drawable.more_vert_fill1_wght300),
                        contentDescription = null,
                        tint = Color.White
                    )
                }
                DropdownMenu(expanded = isContextMenuVisible, onDismissRequest = { isContextMenuVisible = false }) {
                    dropDownItems.forEach { item ->
                        DropdownMenuItem(
                            text = {
                                Text(text = item.text)
                            },
                            onClick = {
                                isContextMenuVisible = !isContextMenuVisible
                                onDropDownItemClick(item)
                            }
                        )
                    }
                }
            }

            val textPaddingDp = 12.dp
            val textColor = getTextColor(Blue)
            Text(modifier = Modifier
                .align(Alignment.BottomStart)
                .offset(0.dp, (-4).dp)
                .drawBehind {
                    val textPaddingPx = with(density) { textPaddingDp.toPx() } / 2

                    val path = Path().apply {
                        moveTo(0f, textPaddingPx)
                        lineTo(size.width - textPaddingPx, textPaddingPx)
                        lineTo(size.width - textPaddingPx, size.height - textPaddingPx)
                        lineTo(0f, size.height - textPaddingPx)

                        close()
                    }

                    drawPath(path, Blue)

                }
                .padding(textPaddingDp),
                text = picturesCollection.collectionName,
                color = textColor,
                fontFamily = SentinelPro,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp)
        }
    }
}

data class DropDownCollectionListItem(
    val text: String,
    val eventType: EventType
)

enum class EventType {
    DELETE, HIDE
}