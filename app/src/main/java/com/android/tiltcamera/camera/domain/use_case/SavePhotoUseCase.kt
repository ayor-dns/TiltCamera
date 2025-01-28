package com.android.tiltcamera.camera.domain.use_case

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.core.domain.DataError
import com.android.tiltcamera.core.domain.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.OutputStream
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

class SavePhotoUseCase(
    private val context: Context,
) {

    suspend operator fun invoke(bitmap: Bitmap, collection: PicturesCollection?, azimuth: Double, pitch: Double, roll: Double): Result<Long, DataError> {
        // save to storage and to db
        withContext(Dispatchers.IO) {
            val resolver: ContentResolver = context.applicationContext.contentResolver

            val imageCollection: Uri = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)

            // Publish a new image.
            val extension = "jpg"
            val displayName = getDisplayName(extension, azimuth, pitch, roll)
            val nowTimestamp: Long = System.currentTimeMillis()
            val appName = context.applicationInfo.loadLabel(context.packageManager).toString()
            val description =  String.format(Locale.getDefault(),"Azimuth=%.2f°, Pitch=%.2f°, Roll=%.2f°",azimuth,pitch,roll)
            val imageContentValues: ContentValues = ContentValues().apply {

                put(MediaStore.Images.Media.DISPLAY_NAME, displayName)
                put(MediaStore.Images.Media.MIME_TYPE, "image/$extension")
                put(MediaStore.MediaColumns.DATE_TAKEN, nowTimestamp)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/$appName")
                put(MediaStore.MediaColumns.IS_PENDING, 1)
                put(MediaStore.Images.Media.DATE_ADDED, nowTimestamp)
                put(MediaStore.Images.Media.DATE_MODIFIED, nowTimestamp)
                put(MediaStore.Images.Media.AUTHOR, appName)
                put(MediaStore.Images.Media.DESCRIPTION, description)
            }

            val imageMediaStoreUri: Uri? = resolver.insert(imageCollection, imageContentValues)

            // Write the image data to the new Uri.
            val result = imageMediaStoreUri?.let { uri ->
                kotlin.runCatching {
                    resolver.openOutputStream(uri).use { outputStream: OutputStream? ->
                        checkNotNull(outputStream) { "Couldn't create file for gallery, MediaStore output stream is null" }
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    }

                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)

                    // insert to db
                    // TODO

                    Result.Success(1)
                }.getOrElse { exception: Throwable ->
                    exception.message?.let(::println)
                    resolver.delete(uri, null, null)
                    Timber.e(exception)
                    Result.Error(DataError.Local.UNKNOWN)
                }
            } ?: run {
                Timber.e("Couldn't create file for gallery")
                Result.Error(DataError.Local.UNKNOWN)
            }

            return@withContext result
        }

        return Result.Error(DataError.Local.UNKNOWN)
    }

    private fun getDisplayName(extension: String, azimuth: Double, pitch: Double, roll: Double): String {
        return "${UUID.randomUUID()}_${azimuth.roundToInt()}_${pitch.roundToInt()}_${roll.roundToInt()}.$extension"
    }
}