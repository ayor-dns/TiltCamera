package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.android.tiltcamera.R
import com.android.tiltcamera.camera.domain.AspectRatioMode
import com.android.tiltcamera.camera.domain.model.PicturesCollection
import com.android.tiltcamera.camera.presentation.CameraAction
import com.android.tiltcamera.core.presentation.Grey100
import com.android.tiltcamera.core.presentation.Grey700
import com.android.tiltcamera.core.presentation.UiText
import com.android.tiltcamera.core.presentation.borderBottom
import com.android.tiltcamera.core.presentation.inputErrorTextStyle

@Composable
fun NewCollectionDialog(
    showDialog: Boolean,
    aspectRatioOptions: List<OptionItem>,
    picturesCollection: PicturesCollection,
    nameError: UiText? = null,
    onAction: (CameraAction) -> Unit,
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusRequesterManager = LocalFocusManager.current
    val minHeightRow = 75.dp


    if(showDialog){
        Dialog(
            onDismissRequest = {
                onAction(CameraAction.DismissNewCollectionDialog)
            }
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // title
                    Box(modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center){
                        Text(
                            text = "Ajouter une collection",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    HorizontalDivider(modifier = Modifier.padding(0.dp, 16.dp))

                    // image
                    Image(
                        modifier = Modifier.size(80.dp),
                        painter = painterResource(R.drawable.picturescollection),
                        contentDescription = null
                    )

                    // name
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(minHeightRow),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    modifier = Modifier
                                        .padding(0.dp, 16.dp),
                                    text = "Nom",
                                    color = Grey700,
                                    fontWeight = FontWeight.Light
                                )
                                Spacer(modifier = Modifier.width(16.dp))
                                BasicTextField(
                                    modifier = Modifier
                                        .weight(1f)
                                        .borderBottom(
                                            if (picturesCollection.name.isBlank()) Grey100 else Color.Transparent,
                                            1.dp,
                                            dashLine = true
                                        ),
                                    value = picturesCollection.name,
                                    onValueChange = { name ->
                                        onAction(CameraAction.SetNewCollectionName(name))
                                    },
                                    singleLine = true,
                                    maxLines = 1,
                                    keyboardOptions = KeyboardOptions.Default.copy(
                                        capitalization = KeyboardCapitalization.Sentences,
                                        autoCorrectEnabled = true,
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {
                                            keyboardController?.hide()
                                            focusRequesterManager.clearFocus()
                                        }
                                    ),
                                    textStyle = TextStyle.Default.copy(textAlign = TextAlign.End)
                                )
                            }

                            // error
                            nameError?.let {
                                Text(
                                    modifier = Modifier.align(Alignment.BottomEnd),
                                    text = it.asString(),
                                    style = inputErrorTextStyle
                                )
                            }
                        }
                    }

                    // aspect ratio
                    MultipleOptionsWithLabel(
//                        modifier = Modifier.height(labelHeight),
                        label = "Format",
                        options = aspectRatioOptions,
                        selectedOption = aspectRatioOptions.firstOrNull{ it.data == picturesCollection.aspectRatioMode },
                        onOptionSelected = { option ->
                            val aspectRatioMode = when(option.data){
                                AspectRatioMode.RATIO_16_9 -> AspectRatioMode.RATIO_16_9
                                AspectRatioMode.RATIO_4_3 -> AspectRatioMode.RATIO_4_3
                                else -> AspectRatioMode.RATIO_16_9
                            }
                            onAction(CameraAction.SetNewCollectionAspectRatioMode(aspectRatioMode))
                        }
                    )

                    // resolution


                    // buttons
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 8.dp),
                        horizontalArrangement = Arrangement.End) {
                        Text(modifier = Modifier.clickable {
                            onAction(CameraAction.CancelNewCollectionDialog)
                        },
                            text = "Annuler")

                        Spacer(modifier = Modifier.width(32.dp))

                        Text(modifier = Modifier.clickable {
                            onAction(CameraAction.ConfirmNewCollectionDialog)
                        },
                            text = "Créer")
                    }
                }
            }
        }
    }
}