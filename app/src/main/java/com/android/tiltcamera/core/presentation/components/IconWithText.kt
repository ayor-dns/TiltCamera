package com.android.tiltcamera.core.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.core.presentation.Purple

@Composable
fun IconWithText(
    modifier: Modifier = Modifier,
    icon: Int,
    text: String,
    onClick: () -> Unit = {}
) {
    Column(modifier = modifier
        .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom){
        Icon(
            modifier = Modifier
                .clip(CircleShape)
                .background(Purple)
                .padding(12.dp),
            painter = painterResource(id = icon),
            contentDescription = text,
            tint = Color.White)
        Text(text = text,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(top = 4.dp))

    }
}