package com.android.tiltcamera.camera.domain

import android.util.Size
import androidx.camera.core.CameraSelector

data class CameraResolution(
    val displayName: String,
    val resolution: Size,
    val aspectRatioMode: AspectRatioMode,
    val lensFacing: Int
) {
    companion object {
        fun fromSizeAndLensFacing(size: Size, lensFacing: Int): CameraResolution {
            val aspectRatio = getAspectRatioString(size)

            val aspectRatioMode = when (aspectRatio) {
                AspectRatioMode.RATIO_4_3.displayName -> AspectRatioMode.RATIO_4_3
                AspectRatioMode.RATIO_16_9.displayName -> AspectRatioMode.RATIO_16_9
                else -> AspectRatioMode.RATIO_OTHER
            }

            return CameraResolution(
                displayName = "${size.width}x${size.height} - $aspectRatio (${getMegaPixels(size)})",
                resolution = size,
                aspectRatioMode = aspectRatioMode,
                lensFacing = lensFacing
            )
        }

        fun default(): CameraResolution {
            return CameraResolution(
                displayName = "Resolution inconnue",
                resolution = Size(Int.MAX_VALUE, Int.MAX_VALUE),
                aspectRatioMode = AspectRatioMode.RATIO_4_3,
                lensFacing = CameraSelector.LENS_FACING_BACK
            )
        }
        fun default(aspectRatioMode: AspectRatioMode): CameraResolution {
            return CameraResolution(
                displayName = "Resolution inconnue",
                resolution = Size(Int.MAX_VALUE, Int.MAX_VALUE),
                aspectRatioMode = aspectRatioMode,
                lensFacing = CameraSelector.LENS_FACING_BACK
            )
        }

        fun default(aspectRatioMode: AspectRatioMode, lensFacing: Int): CameraResolution {
            return CameraResolution(
                displayName = "Resolution inconnue",
                resolution = Size(Int.MAX_VALUE, Int.MAX_VALUE),
                aspectRatioMode = aspectRatioMode,
                lensFacing = lensFacing
            )
        }
    }
}
