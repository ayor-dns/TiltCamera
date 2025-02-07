package com.android.tiltcamera.core.domain

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

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

fun getCurrentTimeInMillis(): Long {
    // Get the default system timezone
    val zoneId: ZoneId = ZoneId.systemDefault()

    // Get the current time in the specified timezone
    val currentTime: ZonedDateTime = ZonedDateTime.now(zoneId)

    // Convert ZonedDateTime to Instant and get time in milliseconds
    return currentTime.toInstant().toEpochMilli()

}

fun getCurrentDate(): String {
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    return today.format(formatter)
}