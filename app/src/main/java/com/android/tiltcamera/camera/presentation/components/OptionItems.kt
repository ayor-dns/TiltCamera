package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.core.presentation.Purple

@Composable
fun OptionItems(
    modifier: Modifier = Modifier,
    optionItem: OptionItem,
    isSelected: Boolean,
    onClick: (OptionItem) -> Unit,
) {
    Box(modifier = modifier
        .size(36.dp)
        .clip(CircleShape)
        .background(if(isSelected) Purple else Color.White)
        .clickable { onClick(optionItem)},
        contentAlignment = Alignment.Center
    ) {
        Icon(painter = painterResource(id = optionItem.icon),
            contentDescription = null,
            tint = if(isSelected) Color.White else Color.Black)
    }
}