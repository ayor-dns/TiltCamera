package com.android.tiltcamera.camera.domain.use_case

import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import androidx.core.net.toUri
import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.domain.repository.PictureRepository
import com.android.tiltcamera.core.domain.DataError
import com.android.tiltcamera.core.domain.Result
import com.android.tiltcamera.core.domain.getCurrentTimeInMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.io.OutputStream
import java.util.Locale
import java.util.UUID
import kotlin.math.roundToInt

class SavePhotoUseCase(
    private val context: Context,
    private val pictureRepository: PictureRepository
) {

    suspend operator fun invoke(bitmap: Bitmap, collection: PicturesCollection?, azimuth: Double?, pitch: Double?, roll: Double?): Result<Long, DataError> {
        // save to storage and to db
        val collectionName = if(collection == null) "" else "/${collection.name}"

        return withContext(Dispatchers.IO) {
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
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DCIM + "/$appName" + collectionName)
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


                    Timber.d("imageMediaStoreUri: $imageMediaStoreUri")

                    imageContentValues.clear()
                    imageContentValues.put(MediaStore.MediaColumns.IS_PENDING, 0)
                    resolver.update(uri, imageContentValues, null, null)

                    // insert to db
                    val id = pictureRepository.insertPicture(Picture(
                        pictureId = 0,
                        creationTimestamp = getCurrentTimeInMillis(),
                        pictureUri = imageMediaStoreUri,
                        collectionIdFK = collection?.collectionId,
                        azimuth = azimuth,
                        pitch = pitch,
                        roll = roll
                    ))

                    Result.Success(id)
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

//        return Result.Error(DataError.Local.UNKNOWN)
    }

    private fun getDisplayName(extension: String, azimuth: Double?, pitch: Double?, roll: Double?): String {
        val azimuthString = azimuth?.let {
            String.format(Locale.US,"%.1f",azimuth)
        } ?: "nan"
        val pitchString = pitch?.let {
            String.format(Locale.US,"%.1f",pitch)
        } ?: "nan"
        val rollString = roll?.let {
            String.format(Locale.US,"%.1f",roll)
        } ?: "nan"

        return "${UUID.randomUUID()}_a${azimuthString}_p${pitchString}_r${rollString}.$extension"
    }
}