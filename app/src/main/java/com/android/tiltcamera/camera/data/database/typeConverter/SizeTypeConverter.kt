package com.android.tiltcamera.camera.data.database.typeConverter

import android.util.Size
import androidx.room.TypeConverter


class SizeConverter {

    @TypeConverter
    fun sizeToString(size: Size): String {
        return "${size.width}x${size.height}"
    }

    @TypeConverter
    fun stringToSize(sizeString: String): Size {
        val (width, height) = sizeString.split("x").map { it.toInt() }
        return Size(width, height)
    }
}