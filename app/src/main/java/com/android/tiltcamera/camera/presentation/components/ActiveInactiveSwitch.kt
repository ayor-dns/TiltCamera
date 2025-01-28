package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.android.tiltcamera.core.domain.getTextColor
import com.android.tiltcamera.core.presentation.Grey300

@Composable
fun ActiveInactiveSwitch(
    modifier: Modifier = Modifier,
    label: String,
    activeLabel: String,
    inactiveLabel: String,
    isActive: Boolean,
    checkedTrackColor: Color,
    checkedThumbColor: Color = getTextColor(checkedTrackColor),
    onCheckedChange: (isActive: Boolean) -> Unit,
    tooltipText: String? = null,
) {
    Row(modifier = modifier
        .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        // ROW LABEL
        LabelWithTooltip(
            modifier = Modifier.weight(1f),
            label = label,
            tooltipText
        )

        // SWITCH WITH LABELS
        Row(modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically) {

            // INACTIVE LABEL
            Text(text = inactiveLabel,
                fontSize = 12.sp,
                color = if(isActive) Grey300 else Color.Black)

            Switch(
                modifier = Modifier.padding(16.dp, 8.dp),
                checked = isActive,
                onCheckedChange = { onCheckedChange(!isActive) },
                colors = SwitchDefaults.colors(
                    checkedTrackColor = checkedTrackColor,
                    checkedThumbColor = checkedThumbColor
                )
            )

            // ACTIVE LABEL
            Text(text = activeLabel,
                fontSize = 12.sp,
                color = if(isActive) Color.Black else Grey300)
        }
    }
}