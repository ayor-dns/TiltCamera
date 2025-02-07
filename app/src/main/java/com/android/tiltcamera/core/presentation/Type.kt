package com.android.tiltcamera.core.presentation

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.Font
import com.android.tiltcamera.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)

val SentinelPro = FontFamily(
    Font(R.font.sentinelpro_light, FontWeight.Light),
    Font(R.font.sentinelpro_lightitalic, FontWeight.Light, FontStyle.Italic),
    Font(R.font.sentinelpro_book, FontWeight.Normal),
    Font(R.font.sentinelpro_bookitalic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.sentinelpro_medium, FontWeight.Medium),
    Font(R.font.sentinelpro_mediumitalic, FontWeight.Medium, FontStyle.Italic),
    Font(R.font.sentinelpro_semibold, FontWeight.SemiBold),
    Font(R.font.sentinelpro_semibolditalic, FontWeight.SemiBold, FontStyle.Italic),
    Font(R.font.sentinelpro_bold, FontWeight.Bold),
    Font(R.font.sentinelpro_bolditalic, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.sentinelpro_black, FontWeight.Black),
    Font(R.font.sentinelpro_blackitalic, FontWeight.Black, FontStyle.Italic)
)

val inputErrorTextStyle = TextStyle(
    fontSize = 12.sp,
    fontWeight = FontWeight.Light,
    lineHeight = 14.sp,
    color = RedError
)
val notEditableText = TextStyle(
    fontSize = 14.sp,
    fontStyle = FontStyle.Italic,
    lineHeight = 14.sp,
    color = Grey500
)
val titleImageDetailBottomSheetTextStyle = TextStyle(
    fontWeight = FontWeight.Bold

)