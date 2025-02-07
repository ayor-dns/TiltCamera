package com.android.tiltcamera.gallery.presentation.picture_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.core.presentation.pictureInfoItemBottomTextStyle
import com.android.tiltcamera.core.presentation.pictureInfoItemTopTextStyle

@Composable
fun PictureInfoItem(
    modifier: Modifier = Modifier,
    leadingIcon: Int,
    iconColor: Color = Color.Black,
    topText: String,
    bottomText: String? = null
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically){

        Icon(
            painter = painterResource(id = leadingIcon),
            contentDescription = null,
            tint = iconColor
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = topText, style = pictureInfoItemTopTextStyle)
            bottomText?.let {
                Text(text = it, style = pictureInfoItemBottomTextStyle)
            }
        }
    }
}