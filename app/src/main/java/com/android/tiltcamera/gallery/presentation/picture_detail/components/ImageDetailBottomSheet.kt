package com.android.tiltcamera.gallery.presentation.picture_detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.domain.model.Picture
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.core.presentation.TimePattern
import com.android.tiltcamera.core.presentation.formatMillisToDateTime
import com.android.tiltcamera.core.presentation.titleImageDetailBottomSheetTextStyle
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState


@Composable
fun ImageDetailBottomSheet(
    modifier: Modifier = Modifier,
    picture: Picture,
    collection: PicturesCollection? = null
) {


    Column(
        modifier = modifier.padding(16.dp)
    ) {
        // DATE
        Text(
            text = formatMillisToDateTime(picture.creationTimestamp, timePattern = TimePattern.NAMED_DAY_AND_NAMED_MONTH_YEAR_TIME),
            style = titleImageDetailBottomSheetTextStyle
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Current collection
        Text(
            text = "Collection",
            style = titleImageDetailBottomSheetTextStyle
        )
        Text(text = collection?.collectionName ?: "Pas de collection")
        Spacer(modifier = Modifier.height(16.dp))

        // map & lat long
        if(picture.latitude != null && picture.longitude != null) {
            val properties = MapProperties(isMyLocationEnabled = false, mapType = MapType.NORMAL)
            val uiSettings = remember {
                MapUiSettings(
                    myLocationButtonEnabled = true,
                    zoomControlsEnabled = false,
                    rotationGesturesEnabled = false,
                )
            }
            val marker by remember { mutableStateOf(LatLng(picture.latitude, picture.longitude)) }
            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(marker, 12f)
            }
            val markerState = rememberUpdatedMarkerState( position = marker)

            Text(
                text = "Localisation",
                style = titleImageDetailBottomSheetTextStyle
            )

            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .height(200.dp),
                properties = properties,
                uiSettings = uiSettings,
                cameraPositionState = cameraPositionState
            ) {
                Marker(
                    state = markerState,
                    draggable = false,
                )
            }
            // values

            Spacer(modifier = Modifier.height(16.dp))
        }


        Text(
            text = "Détails",
            style = titleImageDetailBottomSheetTextStyle
        )

        // name, mpx, width and height
        PictureInfoItem(
            leadingIcon = R.drawable.image_fill0_wght300,
            topText = picture.pictureName,
            bottomText = " Mpx • ${picture.width} x ${picture.height}"
        )

        // azimuth pitch roll
        PictureInfoItem(
            leadingIcon = R.drawable.image_fill0_wght300,
            topText = "Orientation",
            bottomText = "azimuth: ${picture.azimuth} • pitch: ${picture.pitch} • roll: ${picture.roll}"
        )


    }
}