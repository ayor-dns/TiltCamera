package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.model.CameraResolution
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.presentation.CameraAction
import com.android.tiltcamera.core.presentation.Purple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraOptionBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onAction: (CameraAction) -> Unit,
    currentCollection: PicturesCollection?,
    collections: List<PicturesCollection>,
    currentResolution: CameraResolution,
    resolutions: List<CameraResolution>,
    showPictureInfo: Boolean,
    aspectRatioOptions: List<OptionItem>,
    currentAspectRatio: OptionItem?
) {
    val labelHeight by remember { mutableStateOf(60.dp) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(32.dp, 0.dp, 32.dp, 56.dp)) {
            Text(text = "Paramètres")
            HorizontalDivider(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 8.dp))

            // change collection
            DropDownMenuWithLabel(
                modifier = Modifier.height(labelHeight),
                label = "Collection",
                enabled = true,
                items = collections,
                selectedItemToString = { it.name },
                selectedIndex = collections.indexOf(currentCollection),
                onItemSelected = { _, pictureCollection ->
                    onAction(CameraAction.OnPictureCollectionSelected(pictureCollection))
                }
            )
            HorizontalDivider()

            // change format
            MultipleOptionsWithLabel(
                modifier = Modifier.height(labelHeight),
                label = "Format",
                options = aspectRatioOptions,
                selectedOption = currentAspectRatio,
                onOptionSelected = { option ->
                    val aspectRatioMode = when(option.data){
                        AspectRatioMode.RATIO_16_9 -> AspectRatioMode.RATIO_16_9
                        AspectRatioMode.RATIO_4_3 -> AspectRatioMode.RATIO_4_3
                        else -> AspectRatioMode.RATIO_16_9
                    }
                    onAction(CameraAction.SetAspectRatioMode(aspectRatioMode))
                }
            )

            HorizontalDivider()

            // change resolution
            DropDownMenuWithLabel(
                modifier = Modifier.height(labelHeight),
                label = "Resolution",
                enabled = true,
                items = resolutions,
                selectedItemToString = { it.displayName },
                selectedIndex = resolutions.indexOf(currentResolution),
                onItemSelected = { _, cameraResolution ->
                    onAction(CameraAction.OnCameraResolutionSelected(cameraResolution))
                }
            )
            HorizontalDivider()

            // hide / show picture info
            ActiveInactiveSwitch(
                modifier = Modifier.height(labelHeight),
                label = "Afficher Informations",
                activeLabel = "Oui",
                inactiveLabel = "Non",
                isActive = showPictureInfo,
                checkedTrackColor = Purple,
                onCheckedChange = { showPictureInfo ->
                    onAction(CameraAction.SetShowPictureInfo(showPictureInfo))
                }
            )
        }
    }
}