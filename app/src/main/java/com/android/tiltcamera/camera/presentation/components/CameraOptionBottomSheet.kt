package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.presentation.CameraAction
import com.android.tiltcamera.camera.presentation.CameraScreenState
import com.android.tiltcamera.core.presentation.Blue
import com.android.tiltcamera.core.presentation.Purple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraOptionBottomSheet(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    onAction: (CameraAction) -> Unit,
    state: CameraScreenState,
) {
    val labelHeight by remember { mutableStateOf(60.dp) }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = sheetState,
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(32.dp, 0.dp, 32.dp, 56.dp)) {
            Text(text = "Paramètres")
            HorizontalDivider(modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 0.dp))

            // change collection
            DropDownMenuWithLabel(
                modifier = Modifier.height(50.dp),
                label = "Collection",
                enabled = state.collections.isNotEmpty(),
                items = state.collections,
                selectedItemToString = { it.name },
                selectedIndex = state.collections.indexOf(state.currentCollection),
                onItemSelected = { _, pictureCollection ->
                    onAction(CameraAction.OnPictureCollectionSelected(pictureCollection))
                }
            )

            Row(modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
                ){
                Row(
                    modifier = Modifier.clickable {
                        onAction(CameraAction.ShowNewCollectionDialog)
                    },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End){
                    Icon(
                        modifier = Modifier.size(16.dp),
                        painter = painterResource(id = R.drawable.add_fill0_wght300),
                        contentDescription = null,
                        tint = Blue
                    )
                    Spacer(modifier = Modifier.width(4.dp))

                    Text(
                        text = "Nouvelle collection",
                        fontWeight = FontWeight.Light,
                        fontSize = 12.sp,
                        color = Blue
                    )
                }
            }

            HorizontalDivider()

            // change format
            MultipleOptionsWithLabel(
                modifier = Modifier.height(labelHeight),
                label = "Format",
                options = state.aspectRatioOptions,
                selectedOption = state.aspectRatioOptions.firstOrNull { it.data == state.currentAspectRatioMode },
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
                items = state.availableResolutions,
                selectedItemToString = { it.displayName },
                selectedIndex = state.availableResolutions.indexOf(state.currentResolution),
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
                isActive = state.showPictureInfo,
                checkedTrackColor = Purple,
                onCheckedChange = { showPictureInfo ->
                    onAction(CameraAction.SetShowPictureInfo(showPictureInfo))
                }
            )
        }
    }
}