package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.core.presentation.Blue

@Composable
fun TextButtonWithLabel(
    modifier: Modifier = Modifier,
    label: String = "",
    buttonLabel: String,
    icon: Int? = null,
    onClick: () -> Unit,
    tooltipText: String? = null
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

        Row(modifier = Modifier.clickable { onClick() },
            verticalAlignment = Alignment.CenterVertically){
            icon?.let{
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = Blue
                    )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Text(
                text = buttonLabel,
                fontWeight = FontWeight.Light,
                color = Blue
            )
        }

    }
}