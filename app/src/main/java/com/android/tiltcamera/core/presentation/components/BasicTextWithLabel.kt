package com.android.tiltcamera.core.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.android.tiltcamera.camera.presentation.components.LabelWithTooltip
import com.android.tiltcamera.core.presentation.UiText
import com.android.tiltcamera.core.presentation.inputErrorTextStyle
import com.android.tiltcamera.core.presentation.notEditableText

@Composable
fun BasicTextWithLabel(
    modifier: Modifier = Modifier,
    label: String,
    text: String?,
    tooltipText: String? = null,
    isError: UiText? = null
) {
    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LabelWithTooltip(
            modifier = Modifier.weight(1f),
            label = label,
            tooltipText = tooltipText
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(modifier = Modifier.fillMaxWidth(),
                text = text ?: "",
                style = notEditableText,
                textAlign = TextAlign.End)
            // error
            isError?.let {
                Text(modifier = Modifier,
                    text = it.asString(),
                    style = inputErrorTextStyle
                )
            }
        }
    }
}