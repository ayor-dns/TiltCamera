package com.android.tiltcamera.core.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}

fun getTextColor(backgroundColor: Color): Color {
    val luminance1 = backgroundColor.luminance()
    val luminance2 = 1.0f - luminance1
    val contrastRatio = (luminance1 + 0.05) / (luminance2 + 0.05)
    val textColor = if (contrastRatio > 5) Color.Black else Color.White
    return textColor
}