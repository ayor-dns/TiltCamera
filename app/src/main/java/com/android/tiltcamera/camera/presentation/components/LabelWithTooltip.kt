package com.android.tiltcamera.camera.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.RichTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.android.tiltcamera.R
import com.android.tiltcamera.core.presentation.Grey500
import com.android.tiltcamera.core.presentation.Grey700
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LabelWithTooltip(
    modifier: Modifier = Modifier,
    label: String,
    tooltipText: String? = null,
) {
    val tooltipState = rememberTooltipState(isPersistent = true)
    val scope = rememberCoroutineScope()


    Row(modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // ICON ?



        //LABEL
        Text(modifier = Modifier.padding(0.dp, 16.dp),
            text = label,
            color = Grey700,
            fontWeight = FontWeight.Light
        )

//        Spacer(modifier = Modifier.width(8.dp))

        // TOOLTIP
        if(tooltipText != null) {
            TooltipBox(modifier = Modifier,
                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                tooltip = {
                    RichTooltip() {
                        Text(tooltipText)
                    }
                },
                state = tooltipState
            ) {
                IconButton(
                    onClick = { scope.launch { tooltipState.show() } }) {
                    Icon(
                        painter = painterResource(id = R.drawable.info_fill0_wght200),
                        contentDescription = null,
                        tint = Grey500
                    )
                }
            }

        }
    }
}