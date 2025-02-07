package com.android.tiltcamera.gallery.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.android.tiltcamera.R

@Composable
fun BackTopBar(
    modifier: Modifier = Modifier,
    title: String? = null,
    onBackClick: () -> Unit = {}
) {
    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start) {

        IconButton( onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_back_fill0_wght300),
                contentDescription = "back",
                tint = Color.White
            )
        }

        title?.let{
            Text(text = it, color = Color.White)
        }
    }
}