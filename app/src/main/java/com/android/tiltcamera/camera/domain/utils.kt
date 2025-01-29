package com.android.tiltcamera.camera.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Size
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.core.content.ContextCompat
import timber.log.Timber
import java.util.Locale

fun takePhoto(
    context: Context,
    controller: LifecycleCameraController,
    onPhotoTaken: (Bitmap) -> Unit
) {
    controller.takePicture(
        ContextCompat.getMainExecutor(context),
        object : ImageCapture.OnImageCapturedCallback() {
            override fun onCaptureSuccess(image: ImageProxy) {
                super.onCaptureSuccess(image)
                Timber.d("onCaptureSuccess")

                val matrix = Matrix().apply {
                    postRotate(image.imageInfo.rotationDegrees.toFloat())
                }
                val rotatedBitmap = Bitmap.createBitmap(
                    image.toBitmap(),
                    0,
                    0,
                    image.width,
                    image.height,
                    matrix,
                    true
                )

                onPhotoTaken(rotatedBitmap)
                image.close()
            }

            override fun onError(exception: ImageCaptureException) {
                super.onError(exception)
                Timber.e(exception)
            }
        }
    )
}


fun getMegaPixels(resolution: Size): String {
    val megaPixels = (resolution.width * resolution.height) / 1_000_000.0 // 1 mégapixel = 1 million de pixels
    return String.format(Locale.getDefault(), "%.1f MP", megaPixels)
}

fun getAspectRatioString(resolution: Size): String {
    val gcd = gcd(resolution.width, resolution.height)
    val widthRatio = resolution.width / gcd
    val heightRatio = resolution.height / gcd
    return "$widthRatio:$heightRatio"
}

private fun gcd(a: Int, b: Int): Int {
    return if (b == 0) a else gcd(b, a % b)
}