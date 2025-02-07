package com.android.tiltcamera.core.presentation

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.Dp
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

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

fun formatMillisToDateTime(
    millis: Long?,
    timePattern: TimePattern = TimePattern.ISO_DATE_TIME,
    returnedNullValue: String = " - "
): String {
    if (millis == null) {
        return returnedNullValue
    }
    // Convert milliseconds to LocalDateTime
    val dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.systemDefault())

    // Create a DateTimeFormatter with the specified pattern
    val formatter = DateTimeFormatter.ofPattern(timePattern.pattern)

    // Format the LocalDateTime to a string with the desired pattern
    return dateTime.format(formatter)
}

enum class TimePattern(val pattern: String) {
    ISO_DATE_TIME("yyyy-MM-dd HH:mm:ss"),                  // 2024-03-20 12:34:56
    NAMED_DAY_AND_NAMED_MONTH_YEAR_TIME("EEE d MMMM yyyy HH:mm:ss"),     // Mer. 20 mars 2024 12:34:56
    SIMPLE_DATE("d MMMM yyyy"),                            // 20 mars 2024
    DATE_ONLY("yyyy-MM-dd"),                               // 2024-03-20
    TIME_ONLY("HH:mm:ss"),                                 // 12:34:56
}