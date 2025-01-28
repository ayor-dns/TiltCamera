package com.android.tiltcamera.core.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp

fun Modifier.borderBottom(color: Color, width: Dp, dashLine: Boolean = false) = this.then(
    Modifier.drawBehind {
        val strokeWidthPx = width.toPx()
        val y = size.height - strokeWidthPx / 2f
        drawLine(
            color = color,
            start = Offset(0f, y),
            end = Offset(size.width, y),
            strokeWidth = strokeWidthPx,
            pathEffect = if(dashLine) PathEffect.dashPathEffect(floatArrayOf(10f, 5f), 0f) else null
        )
    }
)